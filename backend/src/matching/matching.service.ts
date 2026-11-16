import { BadRequestException, Injectable, UnprocessableEntityException } from '@nestjs/common';
import { Profile } from '@prisma/client';
import { PrismaService } from '../database/prisma.service';
import {
  ANSWER_OPTIONS, CATEGORICAL_MATRICES, CRITERIA_KEYS, INITIAL_WEIGHTS,
  MATCHING_VERSION, ORDINAL_CRITERIA,
} from './matching.constants';
import {
  MatchingProfileInput, NormalizedMatchingProfile, normalizeMatchingProfile,
} from './matching-profile';

export interface CandidateWithScore {
  profile: Profile;
  score: {
    cu: number;
    cv: number;
    cr: number;
    percentage: number; // Điểm tương thích quy đổi %, không phải xác suất Match.
    criteriaScores: Record<string, number>;
    modelVersion: string;
  };
}

@Injectable()
export class MatchingService {
  constructor(private readonly prisma: PrismaService) {}

  private passesConstraints(u: NormalizedMatchingProfile, v: NormalizedMatchingProfile): boolean {
    const a = u.preferences;
    const b = v.preferences;
    return a.acceptedGenders.includes(v.gender) &&
      b.acceptedGenders.includes(u.gender) &&
      a.districtIds.some(id => b.districtIds.includes(id)) &&
      Math.max(a.budgetMinVnd, b.budgetMinVnd) <=
        Math.min(a.budgetMaxVnd ?? Infinity, b.budgetMaxVnd ?? Infinity);
  }

  /** Chỉ hồ sơ đủ dữ liệu và thỏa cả ba điều kiện hai phía mới có thể được gợi ý. */
  passHardFiltering(u: MatchingProfileInput, v: MatchingProfileInput): boolean {
    const a = normalizeMatchingProfile(u).profile;
    const b = normalizeMatchingProfile(v).profile;
    return !!a && !!b && this.passesConstraints(a, b);
  }

  calculateCriteriaScores(
    uChoices: Record<string, string>,
    vChoices: Record<string, string>,
  ): Record<string, number> {
    const scores: Record<string, number> = {};
    for (const key of CRITERIA_KEYS) {
      const u = uChoices?.[key];
      const v = vChoices?.[key];
      if (!ANSWER_OPTIONS[key].includes(u) || !ANSWER_OPTIONS[key].includes(v)) {
        throw new BadRequestException('Thiếu hoặc sai đáp án matching: ' + key);
      }
      // Linh hoạt là tập lịch chấp nhận {EARLY, NORMAL, NIGHT}, không phải hạng giữa.
      if (key === 'schedule' && (u === 'FLEXIBLE' || v === 'FLEXIBLE')) {
        scores[key] = 1;
      } else if (ORDINAL_CRITERIA[key]) {
        const ranks = ORDINAL_CRITERIA[key];
        scores[key] = 1 - Math.abs(ranks[u] - ranks[v]) / (ranks._maxRank - 1);
      } else {
        const score = CATEGORICAL_MATRICES[key]?.[u]?.[v];
        if (score === undefined) throw new Error('Thiếu cấu hình ma trận: ' + key);
        scores[key] = score;
      }
    }
    return scores;
  }

  private normalizedWeights(weights: Readonly<Record<string, number>>): Record<string, number> {
    if (!weights || Object.keys(weights).length !== CRITERIA_KEYS.length ||
        CRITERIA_KEYS.some(key => !Object.prototype.hasOwnProperty.call(weights, key) ||
          !Number.isFinite(weights[key]) || weights[key] < 0)) {
      throw new BadRequestException('Trọng số phải gồm đúng 10 số hữu hạn không âm');
    }
    const total = CRITERIA_KEYS.reduce((sum, key) => sum + weights[key], 0);
    if (!Number.isFinite(total) || total <= 0) {
      throw new BadRequestException('Tổng trọng số phải lớn hơn 0');
    }
    return CRITERIA_KEYS.reduce((result, key) => {
      result[key] = weights[key] / total; // Giữ nguyên trọng số 0.
      return result;
    }, {} as Record<string, number>);
  }

  private scoreProfiles(
    u: NormalizedMatchingProfile,
    v: NormalizedMatchingProfile,
    weightsU: Readonly<Record<string, number>>,
    weightsV: Readonly<Record<string, number>>,
  ): CandidateWithScore['score'] {
    const uv = this.calculateCriteriaScores(u.preferences.answers, v.preferences.answers);
    const vu = this.calculateCriteriaScores(v.preferences.answers, u.preferences.answers);
    const wu = this.normalizedWeights(weightsU);
    const wv = this.normalizedWeights(weightsV);
    const cu = Math.min(1, CRITERIA_KEYS.reduce((sum, key) => sum + wu[key] * uv[key], 0));
    const cv = Math.min(1, CRITERIA_KEYS.reduce((sum, key) => sum + wv[key] * vu[key], 0));
    const cr = cu + cv > 0 ? (2 * cu * cv) / (cu + cv) : 0;
    return {
      cu, cv, cr,
      percentage: Math.round(cr * 100),
      criteriaScores: uv,
      modelVersion: MATCHING_VERSION,
    };
  }

  calculateHarmonicScore(
    u: MatchingProfileInput,
    v: MatchingProfileInput,
    weightsU: Readonly<Record<string, number>> = INITIAL_WEIGHTS,
    weightsV: Readonly<Record<string, number>> = weightsU,
  ): CandidateWithScore['score'] {
    const a = normalizeMatchingProfile(u).profile;
    const b = normalizeMatchingProfile(v).profile;
    if (!a || !b) throw new BadRequestException('Hồ sơ chưa đủ dữ liệu để tính tương thích');
    return this.scoreProfiles(a, b, weightsU, weightsV);
  }

  async getTopCandidates(cognitoSub: string, limit = 20): Promise<CandidateWithScore[]> {
    if (!Number.isInteger(limit) || limit < 1 || limit > 100) {
      throw new BadRequestException('limit phải là số nguyên từ 1 đến 100');
    }
    const currentUser = await this.prisma.profile.findUnique({ where: { cognitoSub } });
    const normalized = currentUser
      ? normalizeMatchingProfile(currentUser)
      : { profile: null, missingFields: ['profile'] };
    if (!normalized.profile) {
      throw new UnprocessableEntityException({
        code: 'MATCHING_PROFILE_INCOMPLETE',
        message: 'Vui lòng hoàn thiện giới tính mong muốn, khu vực TP.HCM, ngân sách và 10 câu trả lời sinh hoạt.',
        missingFields: normalized.missingFields,
      });
    }
    const candidates = await this.prisma.profile.findMany({
      where: {
        cognitoSub: { not: cognitoSub },
        receivedInterests: { none: { senderId: currentUser.id } },
        firstConversations: { none: { secondProfileId: currentUser.id } },
        secondConversations: { none: { firstProfileId: currentUser.id } },
      },
    });
    const ranked: CandidateWithScore[] = [];
    for (const candidate of candidates) {
      const other = normalizeMatchingProfile(candidate).profile;
      if (!other || !this.passesConstraints(normalized.profile, other)) continue;
      ranked.push({
        profile: candidate,
        score: this.scoreProfiles(normalized.profile, other, INITIAL_WEIGHTS, INITIAL_WEIGHTS),
      });
    }
    // Giữ toàn bộ độ chính xác khi xếp hạng; id giúp thứ tự ổn định khi điểm bằng nhau.
    ranked.sort((a, b) => b.score.cr - a.score.cr ||
      (a.profile.id < b.profile.id ? -1 : a.profile.id > b.profile.id ? 1 : 0));
    return ranked.slice(0, limit); // Có ít hơn K ứng viên hợp lệ thì trả ít hơn K.
  }
}

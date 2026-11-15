import { Injectable, Logger } from '@nestjs/common';
import { PrismaService } from '../database/prisma.service';
import {
  INITIAL_WEIGHTS,
  ORDINAL_CRITERIA,
  CATEGORICAL_MATRICES,
} from './matching.constants';

export interface BudgetRange {
  min: number;
  max: number;
}

export interface CandidateWithScore {
  profile: any;
  score: {
    cu: number;    // Điểm từ góc nhìn u đối với v: C_u(v)
    cv: number;    // Điểm từ góc nhìn v đối với u: C_v(u)
    cr: number;    // Điểm tổng hợp điều hòa hai chiều: C^R(u, v)
    percentage: number; // Tỷ lệ % tương thích (0 - 100)
    criteriaScores: Record<string, number>; // Điểm chi tiết 10 tiêu chí
  };
}

@Injectable()
export class MatchingService {
  private readonly logger = new Logger(MatchingService.name);

  constructor(private readonly prisma: PrismaService) {}

  /**
   * Parse khoảng ngân sách từ chuỗi thành khoảng số [min, max] (đơn vị: triệu đồng)
   * Ví dụ: "2.0 - 4.0 triệu/tháng", "Dưới 2 triệu", "Trên 8 triệu/tháng", "3 - 5 triệu"
   */
  parseBudgetRange(budgetStr?: string | null): BudgetRange {
    if (!budgetStr) return { min: 0, max: 20 };

    const cleanStr = budgetStr.toLowerCase();

    if (cleanStr.includes('dưới')) {
      const match = cleanStr.match(/(\d+(\.\d+)?)/);
      const val = match ? parseFloat(match[1]) : 2;
      return { min: 0, max: val };
    }

    if (cleanStr.includes('trên')) {
      const match = cleanStr.match(/(\d+(\.\d+)?)/);
      const val = match ? parseFloat(match[1]) : 8;
      return { min: val, max: 20 };
    }

    const matches = cleanStr.match(/(\d+(\.\d+)?)/g);
    if (matches && matches.length >= 2) {
      return {
        min: parseFloat(matches[0]),
        max: parseFloat(matches[1]),
      };
    } else if (matches && matches.length === 1) {
      const val = parseFloat(matches[0]);
      return { min: Math.max(0, val - 1), max: val + 1 };
    }

    return { min: 0, max: 20 };
  }

  /**
   * 2.1. Lọc ứng viên có thể ở cùng (Hard Filtering)
   * Kiểm tra đồng thời:
   * 1. Giới tính hai chiều
   * 2. Khu vực tìm kiếm
   * 3. Ngân sách cá nhân giao nhau: max(Lu, Lv) <= min(Uu, Uv)
   */
  passHardFiltering(u: any, v: any): boolean {
    // 1. Kiểm tra giới tính hai chiều
    // u.lifestyle thường chứa lựa chọn giới tính (hoặc u.traits)
    // Ví dụ: genderPref: "Nam", "Nữ", "Mọi người"
    const uGenderPref = this.extractGenderPref(u);
    const vGenderPref = this.extractGenderPref(v);

    if (uGenderPref !== 'Mọi người' && uGenderPref !== v.gender) {
      return false;
    }
    if (vGenderPref !== 'Mọi người' && vGenderPref !== u.gender) {
      return false;
    }

    // 2. Kiểm tra khu vực
    // Nếu cả hai đều chọn location, kiểm tra xem có cùng quận/thành phố hoặc chuỗi giao nhau không
    if (u.location && v.location) {
      const uLoc = u.location.toLowerCase();
      const vLoc = v.location.toLowerCase();
      const uTokens = uLoc.split(/[,;\-\/]+/).map((t: string) => t.trim()).filter(Boolean);
      const vTokens = vLoc.split(/[,;\-\/]+/).map((t: string) => t.trim()).filter(Boolean);

      const hasCommonLocation = uTokens.some((ut: string) =>
        vTokens.some((vt: string) => ut.includes(vt) || vt.includes(ut))
      );

      // Nếu không có quận/khu vực chung và cả hai đều chỉ định rõ vị trí
      if (!hasCommonLocation && !uLoc.includes('tất cả') && !vLoc.includes('tất cả')) {
        // Cho phép mềm nếu cùng thành phố (ví dụ HCM / Hà Nội)
        const isSameCity = (uLoc.includes('hcm') && vLoc.includes('hcm')) ||
                           (uLoc.includes('hồ chí minh') && vLoc.includes('hồ chí minh')) ||
                           (uLoc.includes('hà nội') && vLoc.includes('hà nội'));
        if (!isSameCity) {
          return false;
        }
      }
    }

    // 3. Ngân sách cá nhân giao nhau theo công thức (1): max(Lu, Lv) <= min(Uu, Uv)
    const uBudget = this.parseBudgetRange(u.budget);
    const vBudget = this.parseBudgetRange(v.budget);

    const maxL = Math.max(uBudget.min, vBudget.min);
    const minU = Math.min(uBudget.max, vBudget.max);

    if (maxL > minU) {
      return false; // Khoảng ngân sách không giao nhau
    }

    return true;
  }

  /**
   * Trích xuất thông tin sở thích từ Profile (chuỗi traits / lifestyle / needType)
   */
  private extractChoices(profile: any): Record<string, string> {
    const combined = `${profile.lifestyle || ''}; ${profile.traits || ''}; ${profile.needType || ''}`;
    const result: Record<string, string> = {};

    // 1. Giờ giấc
    if (combined.includes('Dậy sớm')) result.schedule = 'Dậy sớm';
    else if (combined.includes('Cú đêm')) result.schedule = 'Cú đêm';
    else if (combined.includes('Linh hoạt')) result.schedule = 'Linh hoạt';
    else result.schedule = 'Bình thường';

    // 2. Tiếng ồn
    if (combined.includes('Rất yên tĩnh')) result.noise = 'Rất yên tĩnh';
    else if (combined.includes('Không ngại ồn')) result.noise = 'Không ngại ồn';
    else if (combined.includes('Bình thường')) result.noise = 'Bình thường';
    else result.noise = 'Khá yên tĩnh';

    // 3. Sạch sẽ
    if (combined.includes('Rất gọn gàng')) result.cleanliness = 'Rất gọn gàng';
    else if (combined.includes('Khá thoải mái')) result.cleanliness = 'Khá thoải mái';
    else if (combined.includes('Bình thường')) result.cleanliness = 'Bình thường';
    else result.cleanliness = 'Khá gọn gàng';

    // 4. Hút thuốc
    if (combined.includes('Thường xuyên')) result.smoking = 'Thường xuyên';
    else if (combined.includes('Thỉnh thoảng')) result.smoking = 'Thỉnh thoảng';
    else result.smoking = 'Không hút';

    // 5. Thú cưng
    if (combined.includes('Không muốn') || combined.includes('Dị ứng')) result.pets = 'Không muốn';
    else if (combined.includes('Thích thú cưng') || combined.includes('Yêu động vật')) result.pets = 'Thích thú cưng';
    else if (combined.includes('Không quan tâm')) result.pets = 'Không quan tâm';
    else result.pets = 'Có thể ở cùng';

    // 6. Khách đến phòng
    if (combined.includes('Hầu như không')) result.guests = 'Hầu như không';
    else if (combined.includes('Thường xuyên')) result.guests = 'Thường xuyên';
    else if (combined.includes('Cuối tuần')) result.guests = 'Cuối tuần';
    else result.guests = 'Thỉnh thoảng';

    // 7. Quyền riêng tư
    if (combined.includes('Rất cao')) result.privacy = 'Rất cao';
    else if (combined.includes('Thoải mái')) result.privacy = 'Thoải mái';
    else if (combined.includes('Vừa phải')) result.privacy = 'Vừa phải';
    else result.privacy = 'Khá cao';

    // 8. Nấu ăn
    if (combined.includes('Tự nấu hàng ngày')) result.cooking = 'Tự nấu hàng ngày';
    else if (combined.includes('Không nấu ăn')) result.cooking = 'Không nấu ăn';
    else if (combined.includes('Hiếm khi nấu')) result.cooking = 'Hiếm khi nấu';
    else result.cooking = 'Nấu cuối tuần';

    // 9. Chia chi phí
    if (combined.includes('Dùng bao nhiêu trả bấy nhiêu')) result.costSharing = 'Dùng bao nhiêu trả bấy nhiêu';
    else if (combined.includes('Linh hoạt')) result.costSharing = 'Linh hoạt';
    else result.costSharing = 'Chia đều tất cả';

    // 10. Trách nhiệm việc chung
    if (combined.includes('Rất chủ động')) result.responsibility = 'Rất chủ động';
    else if (combined.includes('Linh hoạt')) result.responsibility = 'Linh hoạt';
    else if (combined.includes('Bình thường')) result.responsibility = 'Bình thường';
    else result.responsibility = 'Theo phân công';

    return result;
  }

  private extractGenderPref(profile: any): string {
    const combined = `${profile.lifestyle || ''} ${profile.needType || ''}`;
    if (combined.includes('Nữ') && !combined.includes('Mọi người')) return 'Nữ';
    if (combined.includes('Nam') && !combined.includes('Mọi người')) return 'Nam';
    return 'Mọi người';
  }

  /**
   * 2.2. Tính 10 điểm tương thích c_i(u, v) thuộc [0, 1]
   */
  calculateCriteriaScores(uChoices: Record<string, string>, vChoices: Record<string, string>): Record<string, number> {
    const scores: Record<string, number> = {};

    for (const key of Object.keys(INITIAL_WEIGHTS)) {
      const uVal = uChoices[key];
      const vVal = vChoices[key];

      // Trường hợp 1: Dữ liệu thứ bậc (Ordinal)
      if (ORDINAL_CRITERIA[key]) {
        const config = ORDINAL_CRITERIA[key];
        const rankU = config[uVal] || Math.ceil(config._maxRank / 2);
        const rankV = config[vVal] || Math.ceil(config._maxRank / 2);
        const k = config._maxRank;

        // Công thức Podani: c_i(u,v) = 1 - |rank(u) - rank(v)| / (K - 1)
        const ci = 1 - Math.abs(rankU - rankV) / (k - 1);
        scores[key] = Math.max(0, Math.min(1, ci));
      }
      // Trường hợp 2: Dữ liệu danh mục ngữ nghĩa (Categorical)
      else if (CATEGORICAL_MATRICES[key]) {
        const matrix = CATEGORICAL_MATRICES[key];
        const row = matrix[uVal];
        let ci = row ? row[vVal] : undefined;
        if (ci === undefined) {
          // Mặc định trung hòa nếu giá trị mới chưa có trong ma trận
          ci = uVal === vVal ? 1.0 : 0.7;
        }
        scores[key] = Math.max(0, Math.min(1, ci));
      } else {
        scores[key] = uVal === vVal ? 1.0 : 0.7;
      }
    }

    return scores;
  }

  /**
   * 2.5. Tính điểm hai chiều và điểm tổng hợp điều hòa C^R(u, v)
   */
  calculateHarmonicScore(
    u: any,
    v: any,
    weights: Record<string, number> = INITIAL_WEIGHTS
  ): CandidateWithScore['score'] {
    const uChoices = this.extractChoices(u);
    const vChoices = this.extractChoices(v);

    // c_i(u, v)
    const criteriaScoresUV = this.calculateCriteriaScores(uChoices, vChoices);
    // c_i(v, u)
    const criteriaScoresVU = this.calculateCriteriaScores(vChoices, uChoices);

    // C_u(v) = sum(w_i * c_i(u, v))
    let cu = 0;
    // C_v(u) = sum(w_i * c_i(v, u))
    let cv = 0;

    for (const key of Object.keys(weights)) {
      const w = weights[key] || 0.1;
      cu += w * (criteriaScoresUV[key] ?? 0.7);
      cv += w * (criteriaScoresVU[key] ?? 0.7);
    }

    // Công thức (6): C^R(u, v) = 2 * C_u(v) * C_v(u) / [C_u(v) + C_v(u)]
    let cr = 0;
    if (cu + cv > 0) {
      cr = (2 * cu * cv) / (cu + cv);
    }

    return {
      cu: parseFloat(cu.toFixed(4)),
      cv: parseFloat(cv.toFixed(4)),
      cr: parseFloat(cr.toFixed(4)),
      percentage: Math.round(cr * 100),
      criteriaScores: criteriaScoresUV,
    };
  }

  /**
   * Gợi ý danh sách ứng viên đã xếp hạng Top-K (Cold Start)
   */
  async getTopCandidates(cognitoSub: string, limit: number = 20): Promise<CandidateWithScore[]> {
    // 1. Lấy profile của user u
    const currentUser = await this.prisma.profile.findUnique({
      where: { cognitoSub },
    });

    if (!currentUser) {
      this.logger.warn(`User with cognitoSub ${cognitoSub} not found`);
      return [];
    }

    // 2. Lấy toàn bộ các ứng viên v khác trong database
    const allCandidates = await this.prisma.profile.findMany({
      where: {
        cognitoSub: { not: cognitoSub },
      },
    });

    const scoredCandidates: CandidateWithScore[] = [];

    for (const candidate of allCandidates) {
      // 3. Lọc điều kiện bắt buộc hai chiều
      const isEligible = this.passHardFiltering(currentUser, candidate);
      if (!isEligible) {
        continue;
      }

      // 4. Tính điểm 10 tiêu chí & trung bình điều hòa hai chiều C^R
      const score = this.calculateHarmonicScore(currentUser, candidate);

      scoredCandidates.push({
        profile: candidate,
        score,
      });
    }

    // 5. Xếp hạng giảm dần theo C^R (u, v)
    scoredCandidates.sort((a, b) => b.score.cr - a.score.cr);

    // Đồng bộ người dùng thực tế: Nếu danh sách sau lọc ít hơn limit và database có các ứng viên khác,
    // bổ sung các ứng viên này để đảm bảo mọi người dùng thực tế đều tìm thấy nhau trên DiscoverScreen
    if (scoredCandidates.length < limit && allCandidates.length > 0) {
      const existingIds = new Set(scoredCandidates.map((c) => c.profile.id));
      for (const candidate of allCandidates) {
        if (!existingIds.has(candidate.id)) {
          const score = this.calculateHarmonicScore(currentUser, candidate);
          scoredCandidates.push({
            profile: candidate,
            score: {
              ...score,
              percentage: Math.max(score.percentage, 60), // Điểm tương thích cơ bản
            },
          });
          existingIds.add(candidate.id);
        }
      }
    }

    return scoredCandidates.slice(0, limit);
  }
}

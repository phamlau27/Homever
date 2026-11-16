import { BadRequestException, Injectable, HttpException, HttpStatus } from '@nestjs/common';
import { Prisma } from '@prisma/client';
import { PrismaService } from '../database/prisma.service';
import {
  legacyQuestionnaireFingerprint, normalizeGender, normalizeMatchingProfile, validateMatchingPreferences,
} from '../matching/matching-profile';
import { CreateProfileDto } from './dto';

@Injectable()
export class UsersService {
  constructor(private prisma: PrismaService) {}

  async getProfile(cognitoSub: string) {
    const profile = await this.prisma.profile.findUnique({ where: { cognitoSub } });
    if (!profile) {
      throw new HttpException({
        code: 'PROFILE_NOT_FOUND',
        message: 'User profile not found',
      }, HttpStatus.NOT_FOUND);
    }
    return profile;
  }

  async createProfile(cognitoSub: string, data: CreateProfileDto) {
    if (data.gender !== undefined && !normalizeGender(data.gender)) {
      throw new BadRequestException('Giới tính không hợp lệ');
    }
    const existing = await this.prisma.profile.findUnique({ where: { cognitoSub } });
    let matchingPreferences: Prisma.InputJsonValue | typeof Prisma.DbNull | undefined;
    if (Object.prototype.hasOwnProperty.call(data, 'matchingPreferences')) {
      const validated = validateMatchingPreferences(data.matchingPreferences);
      if (!validated) {
        throw new BadRequestException({
          code: 'INVALID_MATCHING_PREFERENCES',
          message: 'Lựa chọn matching phải gồm giới tính chấp nhận, khu vực TP.HCM, ngân sách hợp lệ và đủ 10 đáp án.',
        });
      }
      matchingPreferences = validated as unknown as Prisma.InputJsonValue;
    } else {
      // Hỗ trợ client/hồ sơ cũ. Nếu thay đổi lựa chọn cũ, không giữ JSON đã lỗi thời.
      const matchingFields = ['budget', 'location'] as const;
      const changed = !existing || matchingFields.some(key =>
        data[key] !== undefined && data[key] !== existing[key]) ||
        (data.lifestyle !== undefined && legacyQuestionnaireFingerprint(data.lifestyle) !==
          legacyQuestionnaireFingerprint(existing.lifestyle));
      if (changed || !existing.matchingPreferences) {
        const merged = {
          gender: data.gender !== undefined ? data.gender : existing?.gender,
          lifestyle: data.lifestyle !== undefined ? data.lifestyle : existing?.lifestyle,
          budget: data.budget !== undefined ? data.budget : existing?.budget,
          location: data.location !== undefined ? data.location : existing?.location,
        };
        const normalized = normalizeMatchingProfile(merged).profile;
        matchingPreferences = normalized
          ? normalized.preferences as unknown as Prisma.InputJsonValue
          : Prisma.DbNull;
      }
    }
    const profileData = {
      displayName: data.displayName,
      gender: data.gender,
      age: data.age === undefined ? existing?.age ?? 20 : Number(data.age) || 20,
      occupation: data.occupation,
      avatarUrl: data.avatarUrl,
      needType: data.needType,
      lifestyle: data.lifestyle,
      budget: data.budget,
      location: data.location,
      traits: data.traits,
      bio: data.bio,
      matchingPreferences,
    };
    return this.prisma.profile.upsert({
      where: { cognitoSub },
      update: profileData,
      create: { cognitoSub, ...profileData },
    });
  }
}

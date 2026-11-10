import { Injectable, HttpException, HttpStatus } from '@nestjs/common';
import { PrismaService } from '../database/prisma.service';
import { CreateProfileDto } from './dto';

@Injectable()
export class UsersService {
  constructor(private prisma: PrismaService) {}

  async getProfile(cognitoSub: string) {
    const profile = await this.prisma.profile.findUnique({
      where: { cognitoSub },
    });

    if (!profile) {
      throw new HttpException({
        code: 'PROFILE_NOT_FOUND',
        message: 'User profile not found',
      }, HttpStatus.NOT_FOUND);
    }

    return profile;
  }

  async createProfile(cognitoSub: string, data: CreateProfileDto) {
    const profileData = {
      displayName: data.displayName,
      gender: data.gender,
      age: Number(data.age) || 20,
      occupation: data.occupation,
      avatarUrl: data.avatarUrl,
      needType: data.needType,
      lifestyle: data.lifestyle,
      budget: data.budget,
      location: data.location,
      traits: data.traits,
      bio: data.bio,
    };

    return this.prisma.profile.upsert({
      where: { cognitoSub },
      update: profileData,
      create: {
        cognitoSub,
        ...profileData,
      },
    });
  }
}

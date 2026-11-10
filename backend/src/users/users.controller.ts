import { Controller, Get, Post, Body, UseGuards, HttpCode } from '@nestjs/common';
import { UsersService } from './users.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { User } from '../auth/user.decorator';
import { S3Service } from '../storage/s3.service';
import { CreateProfileDto, PresignedUrlDto } from './dto';

@Controller('users')
@UseGuards(JwtAuthGuard)
export class UsersController {
  constructor(
    private readonly usersService: UsersService,
    private readonly s3Service: S3Service,
  ) {}

  @Get('me')
  async getMyProfile(@User() user: any) {
    // user.sub contains the Cognito identity
    return this.usersService.getProfile(user.sub);
  }

  @Post('me')
  @HttpCode(201)
  async createProfile(@User() user: any, @Body() dto: CreateProfileDto) {
    return this.usersService.createProfile(user.sub, dto);
  }

  @Post('me/avatar/presigned-url')
  @HttpCode(200)
  async getPresignedUrl(@User() user: any, @Body() dto: PresignedUrlDto) {
    return this.s3Service.getPresignedUrl(user.sub, dto.contentType, dto.fileName);
  }
}

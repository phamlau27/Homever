import { BadRequestException, Body, Controller, Get, Post, Query, UseGuards } from '@nestjs/common';
import { InterestsService } from './interests.service';
import { MatchingService } from './matching.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { User } from '../auth/user.decorator';

@Controller('matching')
@UseGuards(JwtAuthGuard)
export class MatchingController {
  constructor(
    private readonly matchingService: MatchingService,
    private readonly interestsService: InterestsService,
  ) {}

  @Get('interests')
  getInterests(@User() user: any) {
    return this.interestsService.list(user.sub);
  }

  @Post('interests')
  sendInterest(@User() user: any, @Body() body: { profileId?: string }) {
    return this.interestsService.send(user.sub, body?.profileId ?? '');
  }

  /**
   * GET /v1/matching/candidates?limit=20
   * Trả về danh sách ứng viên đã vượt qua Hard Filtering và xếp hạng theo C^R (Giai đoạn 1)
   */
  @Get('candidates')
  async getCandidates(
    @User() user: any,
    @Query('limit') limit?: string,
  ) {
    if (limit !== undefined && !/^[1-9]\d*$/.test(limit)) {
      throw new BadRequestException('limit phải là số nguyên từ 1 đến 100');
    }
    return this.matchingService.getTopCandidates(user.sub, limit === undefined ? 20 : Number(limit));
  }
}

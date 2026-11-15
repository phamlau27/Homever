import { Controller, Get, Query, UseGuards } from '@nestjs/common';
import { MatchingService } from './matching.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { User } from '../auth/user.decorator';

@Controller('matching')
@UseGuards(JwtAuthGuard)
export class MatchingController {
  constructor(private readonly matchingService: MatchingService) {}

  /**
   * GET /v1/matching/candidates?limit=20
   * Trả về danh sách ứng viên đã vượt qua Hard Filtering và xếp hạng theo C^R (Giai đoạn 1)
   */
  @Get('candidates')
  async getCandidates(
    @User() user: any,
    @Query('limit') limit?: string,
  ) {
    const limitNum = limit ? parseInt(limit, 10) : 20;
    return this.matchingService.getTopCandidates(user.sub, limitNum);
  }
}

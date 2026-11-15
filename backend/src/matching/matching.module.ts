import { Module } from '@nestjs/common';
import { ChatModule } from '../chat/chat.module';
import { MatchingService } from './matching.service';
import { InterestsService } from './interests.service';
import { MatchingController } from './matching.controller';
import { DatabaseModule } from '../database/database.module';
import { AuthModule } from '../auth/auth.module';

@Module({
  imports: [DatabaseModule, AuthModule, ChatModule],
  controllers: [MatchingController],
  providers: [MatchingService, InterestsService],
  exports: [MatchingService],
})
export class MatchingModule {}

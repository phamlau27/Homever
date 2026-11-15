import { Body, Controller, Get, Param, Post, Query, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { User } from '../auth/user.decorator';
import { ChatService } from './chat.service';

@Controller('chat/conversations')
@UseGuards(JwtAuthGuard)
export class ChatController {
  constructor(private readonly chat: ChatService) {}

  @Get()
  list(@User() user: any) {
    return this.chat.conversations(user.sub);
  }

  @Get(':id/messages')
  messages(@User() user: any, @Param('id') id: string, @Query('afterId') after?: string, @Query('beforeId') before?: string) {
    return this.chat.messages(user.sub, id, after, before);
  }

  @Post(':id/messages')
  send(@User() user: any, @Param('id') id: string, @Body() body: { content?: unknown; clientId?: unknown }) {
    return this.chat.send(user.sub, id, body?.content, body?.clientId);
  }

  @Post(':id/read')
  read(@User() user: any, @Param('id') id: string, @Body() body: { lastMessageId?: unknown }) {
    return this.chat.markRead(user.sub, id, body?.lastMessageId);
  }

  @Post(':id/match-seen')
  acknowledge(@User() user: any, @Param('id') id: string) {
    return this.chat.acknowledgeMatch(user.sub, id);
  }
}

import { BadRequestException, ConflictException, ForbiddenException, Injectable, NotFoundException } from '@nestjs/common';
import { Prisma } from '@prisma/client';
import { PrismaService } from '../database/prisma.service';

@Injectable()
export class ChatService {
  constructor(private readonly prisma: PrismaService) {}

  private async profile(cognitoSub: string) {
    const profile = await this.prisma.profile.findUnique({ where: { cognitoSub } });
    if (!profile) throw new NotFoundException('PROFILE_NOT_FOUND');
    return profile;
  }

  private async member(cognitoSub: string, conversationId: string) {
    const profile = await this.profile(cognitoSub);
    const conversation = await this.prisma.conversation.findUnique({ where: { id: conversationId } });
    if (!conversation) throw new NotFoundException('CONVERSATION_NOT_FOUND');
    if (profile.id !== conversation.firstProfileId && profile.id !== conversation.secondProfileId) {
      throw new ForbiddenException('NOT_A_PARTICIPANT');
    }
    return { profile, conversation, first: profile.id === conversation.firstProfileId };
  }

  async connectIfMutual(transaction: Prisma.TransactionClient, senderId: string, receiverId: string) {
    const reciprocal = await transaction.interest.findUnique({
      where: { senderId_receiverId: { senderId: receiverId, receiverId: senderId } },
    });
    if (!reciprocal) return null;
    const [firstProfileId, secondProfileId] = [senderId, receiverId].sort();
    return transaction.conversation.upsert({
      where: { firstProfileId_secondProfileId: { firstProfileId, secondProfileId } },
      create: { firstProfileId, secondProfileId },
      update: {},
    });
  }

  async conversations(cognitoSub: string) {
    const profile = await this.profile(cognitoSub);
    const conversations = await this.prisma.conversation.findMany({
      where: { OR: [{ firstProfileId: profile.id }, { secondProfileId: profile.id }] },
      include: {
        firstProfile: true,
        secondProfile: true,
        messages: { orderBy: { id: 'desc' }, take: 1 },
      },
      orderBy: [{ updatedAt: 'desc' }, { id: 'asc' }],
    });
    const summaries = await Promise.all(conversations.map(async (conversation) => {
      const first = profile.id === conversation.firstProfileId;
      const peer = first ? conversation.secondProfile : conversation.firstProfile;
      const { cognitoSub: _, ...publicProfile } = peer;
      const unreadCount = await this.prisma.chatMessage.count({
        where: {
          conversationId: conversation.id,
          senderId: { not: profile.id },
          id: { gt: first ? conversation.firstReadMessageId : conversation.secondReadMessageId },
        },
      });
      return {
        id: conversation.id,
        profile: publicProfile,
        createdAt: conversation.createdAt,
        lastMessage: conversation.messages[0] ?? null,
        unreadCount,
        pendingMatch: (first ? conversation.firstMatchSeenAt : conversation.secondMatchSeenAt) === null,
      };
    }));
    summaries.sort((first, second) =>
      (second.lastMessage?.createdAt ?? second.createdAt).getTime() -
      (first.lastMessage?.createdAt ?? first.createdAt).getTime() || first.id.localeCompare(second.id));
    return { currentProfileId: profile.id, conversations: summaries };
  }

  async summary(cognitoSub: string, conversationId: string) {
    await this.member(cognitoSub, conversationId);
    const result = await this.conversations(cognitoSub);
    return result.conversations.find((conversation) => conversation.id === conversationId)!;
  }

  async acknowledgeMatch(cognitoSub: string, conversationId: string) {
    const { first } = await this.member(cognitoSub, conversationId);
    await this.prisma.conversation.update({
      where: { id: conversationId },
      data: first ? { firstMatchSeenAt: new Date() } : { secondMatchSeenAt: new Date() },
    });
    return { success: true };
  }

  private cursor(value?: string) {
    if (value === undefined) return undefined;
    if (!/^\d+$/.test(value) || !Number.isSafeInteger(Number(value)) || Number(value) > 2147483647) {
      throw new BadRequestException('INVALID_MESSAGE_CURSOR');
    }
    return Number(value);
  }

  async messages(cognitoSub: string, conversationId: string, after?: string, before?: string) {
    await this.member(cognitoSub, conversationId);
    const afterId = this.cursor(after);
    const beforeId = this.cursor(before);
    if (afterId !== undefined && beforeId !== undefined) throw new BadRequestException('INVALID_MESSAGE_CURSOR');
    const ascending = afterId !== undefined;
    const messages = await this.prisma.chatMessage.findMany({
      where: {
        conversationId,
        ...(afterId !== undefined ? { id: { gt: afterId } } : {}),
        ...(beforeId !== undefined ? { id: { lt: beforeId } } : {}),
      },
      orderBy: { id: ascending ? 'asc' : 'desc' },
      take: 51,
    });
    const hasMore = messages.length > 50;
    const page = messages.slice(0, 50);
    return { messages: ascending ? page : page.reverse(), hasMore };
  }

  async send(cognitoSub: string, conversationId: string, content: unknown, clientId: unknown) {
    const { profile } = await this.member(cognitoSub, conversationId);
    if (typeof content !== 'string' || !content.trim() || content.trim().length > 4000 ||
        typeof clientId !== 'string' || !/^[a-zA-Z0-9-]{1,64}$/.test(clientId)) {
      throw new BadRequestException('INVALID_MESSAGE');
    }
    return this.prisma.$transaction(async (transaction) => {
      await transaction.$queryRaw`SELECT "id" FROM "homever_conversations" WHERE "id" = ${conversationId} FOR UPDATE`;
      const message = await transaction.chatMessage.upsert({
        where: { senderId_clientId: { senderId: profile.id, clientId } },
        create: { senderId: profile.id, conversationId, clientId, content: content.trim() },
        update: {},
      });
      if (message.conversationId !== conversationId || message.content !== content.trim()) {
        throw new ConflictException('CLIENT_ID_REUSED');
      }
      await transaction.conversation.update({
        where: { id: conversationId },
        data: { updatedAt: new Date() },
      });
      return message;
    });
  }

  async markRead(cognitoSub: string, conversationId: string, messageId: unknown) {
    const { first } = await this.member(cognitoSub, conversationId);
    if (!Number.isInteger(messageId) || (messageId as number) <= 0 || (messageId as number) > 2147483647) {
      throw new BadRequestException('INVALID_MESSAGE_ID');
    }
    const message = await this.prisma.chatMessage.findFirst({
      where: { id: messageId as number, conversationId },
    });
    if (!message) throw new BadRequestException('MESSAGE_NOT_IN_CONVERSATION');
    await this.prisma.conversation.updateMany({
      where: {
        id: conversationId,
        ...(first ? { firstReadMessageId: { lt: message.id } } : { secondReadMessageId: { lt: message.id } }),
      },
      data: first ? { firstReadMessageId: message.id } : { secondReadMessageId: message.id },
    });
    return { success: true };
  }
}

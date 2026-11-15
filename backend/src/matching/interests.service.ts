import { BadRequestException, Injectable, NotFoundException } from '@nestjs/common';
import { PrismaService } from '../database/prisma.service';
import { ChatService } from '../chat/chat.service';

@Injectable()
export class InterestsService {
  constructor(private readonly prisma: PrismaService, private readonly chat: ChatService) {}

  private async currentProfile(cognitoSub: string) {
    const profile = await this.prisma.profile.findUnique({ where: { cognitoSub } });
    if (!profile) throw new NotFoundException('PROFILE_NOT_FOUND');
    return profile;
  }

  async send(cognitoSub: string, profileId: string) {
    if (typeof profileId !== 'string' || !profileId.trim()) {
      throw new BadRequestException('PROFILE_ID_REQUIRED');
    }
    const sender = await this.currentProfile(cognitoSub);
    if (sender.id === profileId) throw new BadRequestException('CANNOT_INTEREST_SELF');
    const receiver = await this.prisma.profile.findUnique({ where: { id: profileId } });
    if (!receiver) throw new NotFoundException('PROFILE_NOT_FOUND');
    const pairKey = [sender.id, profileId].sort().join(':');
    const connection = await this.prisma.$transaction(async (transaction) => {
      await transaction.$queryRaw`SELECT pg_advisory_xact_lock(hashtextextended(${pairKey}, 0))::text`;
      await transaction.interest.upsert({
        where: { senderId_receiverId: { senderId: sender.id, receiverId: profileId } },
        create: { senderId: sender.id, receiverId: profileId },
        update: {},
      });
      return this.chat.connectIfMutual(transaction, sender.id, profileId);
    }, { timeout: 15000 });
    const interests = await this.list(cognitoSub);
    const summary = connection ? await this.chat.summary(cognitoSub, connection.id) : null;
    return { ...interests, matchedConversation: summary?.pendingMatch ? summary : null };
  }

  async list(cognitoSub: string) {
    const profile = await this.currentProfile(cognitoSub);
    const [sent, received] = await this.prisma.$transaction([
      this.prisma.interest.findMany({
        where: { senderId: profile.id },
        include: { receiver: true },
        orderBy: [{ createdAt: 'desc' }, { id: 'asc' }],
      }),
      this.prisma.interest.findMany({
        where: { receiverId: profile.id },
        include: { sender: true },
        orderBy: [{ createdAt: 'desc' }, { id: 'asc' }],
      }),
    ]);
    const sentIds = new Set(sent.map((interest) => interest.receiverId));
    const receivedIds = new Set(received.map((interest) => interest.senderId));
    const publicProfile = ({ cognitoSub: _, ...data }: typeof profile) => data;
    return {
      connectedProfileIds: [...sentIds].filter((id) => receivedIds.has(id)),
      sent: sent.filter((interest) => !receivedIds.has(interest.receiverId)).map((interest) => ({
        id: interest.id,
        profile: publicProfile(interest.receiver),
        mutual: receivedIds.has(interest.receiverId),
      })),
      received: received.filter((interest) => !sentIds.has(interest.senderId)).map((interest) => ({
        id: interest.id,
        profile: publicProfile(interest.sender),
        mutual: sentIds.has(interest.senderId),
      })),
    };
  }
}

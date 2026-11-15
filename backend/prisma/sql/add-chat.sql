CREATE TABLE IF NOT EXISTS "homever_conversations" (
    "id" TEXT NOT NULL,
    "firstProfileId" TEXT NOT NULL,
    "secondProfileId" TEXT NOT NULL,
    "firstMatchSeenAt" TIMESTAMP(3),
    "secondMatchSeenAt" TIMESTAMP(3),
    "firstReadMessageId" INTEGER NOT NULL DEFAULT 0,
    "secondReadMessageId" INTEGER NOT NULL DEFAULT 0,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "homever_conversations_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "homever_conversations_firstProfileId_fkey" FOREIGN KEY ("firstProfileId") REFERENCES "homever_profiles"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_conversations_secondProfileId_fkey" FOREIGN KEY ("secondProfileId") REFERENCES "homever_profiles"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_conversations_ordered_pair" CHECK ("firstProfileId" < "secondProfileId")
);
CREATE UNIQUE INDEX IF NOT EXISTS "homever_conversations_firstProfileId_secondProfileId_key" ON "homever_conversations"("firstProfileId", "secondProfileId");
CREATE INDEX IF NOT EXISTS "homever_conversations_secondProfileId_idx" ON "homever_conversations"("secondProfileId");

CREATE TABLE IF NOT EXISTS "homever_messages" (
    "id" SERIAL NOT NULL,
    "conversationId" TEXT NOT NULL,
    "senderId" TEXT NOT NULL,
    "clientId" VARCHAR(64) NOT NULL,
    "content" VARCHAR(4000) NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "homever_messages_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "homever_messages_conversationId_fkey" FOREIGN KEY ("conversationId") REFERENCES "homever_conversations"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_messages_senderId_fkey" FOREIGN KEY ("senderId") REFERENCES "homever_profiles"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_messages_nonempty" CHECK (length(trim("content")) > 0)
);
CREATE UNIQUE INDEX IF NOT EXISTS "homever_messages_senderId_clientId_key" ON "homever_messages"("senderId", "clientId");
CREATE INDEX IF NOT EXISTS "homever_messages_conversationId_id_idx" ON "homever_messages"("conversationId", "id");

INSERT INTO "homever_conversations" ("id", "firstProfileId", "secondProfileId")
SELECT gen_random_uuid()::text, outgoing."senderId", outgoing."receiverId"
FROM "homever_interests" outgoing
JOIN "homever_interests" incoming
    ON incoming."senderId" = outgoing."receiverId" AND incoming."receiverId" = outgoing."senderId"
WHERE outgoing."senderId" < outgoing."receiverId"
ON CONFLICT ("firstProfileId", "secondProfileId") DO NOTHING;

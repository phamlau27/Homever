CREATE TABLE IF NOT EXISTS "homever_interests" (
    "id" TEXT NOT NULL,
    "senderId" TEXT NOT NULL,
    "receiverId" TEXT NOT NULL,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "homever_interests_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "homever_interests_senderId_fkey" FOREIGN KEY ("senderId") REFERENCES "homever_profiles"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_interests_receiverId_fkey" FOREIGN KEY ("receiverId") REFERENCES "homever_profiles"("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "homever_interests_no_self_interest" CHECK ("senderId" <> "receiverId")
);

CREATE UNIQUE INDEX IF NOT EXISTS "homever_interests_senderId_receiverId_key" ON "homever_interests"("senderId", "receiverId");
CREATE INDEX IF NOT EXISTS "homever_interests_receiverId_createdAt_idx" ON "homever_interests"("receiverId", "createdAt");

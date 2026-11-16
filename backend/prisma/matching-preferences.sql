-- Additive, idempotent upgrade for existing Homever databases without migration history.
-- Apply before starting the backend built with the matchingPreferences Prisma field.
ALTER TABLE "homever_profiles"
  ADD COLUMN IF NOT EXISTS "matchingPreferences" JSONB;

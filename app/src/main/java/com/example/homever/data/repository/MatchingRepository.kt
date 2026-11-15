package com.example.homever.data.repository

import com.example.homever.data.model.UserInteraction
import com.example.homever.data.model.UserMatch
import com.example.homever.data.model.UserProfile
import com.example.homever.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatchingRepository {
    private val db = SupabaseClient.client

    // Lấy danh sách hồ sơ để gợi ý ghép phòng (loại trừ bản thân)
    suspend fun getCandidateProfiles(currentUserId: String?): Result<List<UserProfile>> = withContext(Dispatchers.IO) {
        runCatching {
            val query = db.from("profiles").select()
            val profiles = query.decodeList<UserProfile>()
            if (currentUserId.isNullOrBlank()) {
                profiles
            } else {
                profiles.filter { it.id != currentUserId }
            }
        }
    }

    // Ghi nhận lượt tương tác (Like / Dislike)
    suspend fun recordInteraction(fromUserId: String, toUserId: String, isLiked: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val actionStr = if (isLiked) "LIKE" else "DISLIKE"
            val interaction = UserInteraction(
                fromUserId = fromUserId,
                toUserId = toUserId,
                action = actionStr
            )
            // Lưu tương tác vào bảng "user_interactions"
            db.from("user_interactions").insert(interaction)

            if (!isLiked) return@runCatching false

            // Kiểm tra xem đối phương có LIKE lại mình không (tương thích 2 chiều)
            val reciprocal = db.from("user_interactions")
                .select {
                    filter {
                        eq("from_user_id", toUserId)
                        eq("to_user_id", fromUserId)
                        eq("action", "LIKE")
                    }
                }
                .decodeSingleOrNull<UserInteraction>()

            val isMatch = reciprocal != null
            if (isMatch) {
                // Tạo bản ghi match trong bảng "user_matches"
                val matchRecord = UserMatch(
                    userId1 = fromUserId,
                    userId2 = toUserId,
                    isMatched = true
                )
                db.from("user_matches").insert(matchRecord)
            }
            isMatch
        }
    }
}

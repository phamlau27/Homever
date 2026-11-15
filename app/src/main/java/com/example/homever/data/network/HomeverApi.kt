package com.example.homever.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class MatchingScore(
    val cu: Float = 0f,
    val cv: Float = 0f,
    val cr: Float = 0f,
    val percentage: Int = 0,
    val criteriaScores: Map<String, Float> = emptyMap()
)

data class CandidateResponse(
    val profile: UserProfile,
    val score: MatchingScore
)

data class UserProfile(
    val id: String = "",
    val displayName: String = "",
    val gender: String = "",
    val age: Int = 0,
    val occupation: String = "",
    val avatarUrl: String? = null,
    val needType: String? = null,
    val lifestyle: String? = null,
    val budget: String? = null,
    val location: String? = null,
    val traits: String? = null,
    val bio: String? = null
)

data class UploadImageResponse(
    val path: String = "",
    val url: String = ""
)

data class PresignedUrlRequest(
    val fileName: String,
    val contentType: String
)

data class PresignedUrlResponse(
    val uploadUrl: String,
    val fileKey: String
)

data class InterestRequest(val profileId: String)

data class UserInterest(
    val id: String,
    val profile: UserProfile,
    val mutual: Boolean = false
)

data class InterestsResponse(
    val sent: List<UserInterest> = emptyList(),
    val received: List<UserInterest> = emptyList(),
    val connectedProfileIds: List<String> = emptyList(),
    val matchedConversation: ConversationSummary? = null
)

data class ChatMessage(
    val id: Int,
    val conversationId: String,
    val senderId: String,
    val clientId: String,
    val content: String,
    val createdAt: String
)

data class ConversationSummary(
    val id: String,
    val profile: UserProfile,
    val createdAt: String,
    val lastMessage: ChatMessage? = null,
    val unreadCount: Int = 0,
    val pendingMatch: Boolean = false
)

data class ConversationsResponse(
    val currentProfileId: String = "",
    val conversations: List<ConversationSummary> = emptyList()
)

data class ChatMessagesResponse(
    val messages: List<ChatMessage> = emptyList(),
    val hasMore: Boolean = false
)

data class SendMessageRequest(val content: String, val clientId: String)
data class MarkReadRequest(val lastMessageId: Int)

interface HomeverApi {
    @GET("chat/conversations")
    suspend fun getConversations(): Response<ConversationsResponse>

    @GET("chat/conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") conversationId: String,
        @Query("afterId") afterId: Int? = null,
        @Query("beforeId") beforeId: Int? = null
    ): Response<ChatMessagesResponse>

    @POST("chat/conversations/{id}/messages")
    suspend fun sendMessage(@Path("id") conversationId: String, @Body request: SendMessageRequest): Response<ChatMessage>

    @POST("chat/conversations/{id}/read")
    suspend fun markMessagesRead(@Path("id") conversationId: String, @Body request: MarkReadRequest): Response<Unit>

    @POST("chat/conversations/{id}/match-seen")
    suspend fun acknowledgeMatch(@Path("id") conversationId: String): Response<Unit>

    @GET("matching/interests")
    suspend fun getInterests(): Response<InterestsResponse>

    @POST("matching/interests")
    suspend fun sendInterest(@Body request: InterestRequest): Response<InterestsResponse>

    @GET("users/me")
    suspend fun getMyProfile(): Response<UserProfile>

    @POST("users/me")
    suspend fun updateMyProfile(@Body profile: UserProfile): Response<UserProfile>

    @Multipart
    @POST("storage/images")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<UploadImageResponse>

    @POST("users/me/avatar/presigned-url")
    suspend fun getAvatarPresignedUrl(@Body request: PresignedUrlRequest): Response<PresignedUrlResponse>

    @GET("matching/candidates")
    suspend fun getMatchingCandidates(@Query("limit") limit: Int = 20): Response<List<CandidateResponse>>
}

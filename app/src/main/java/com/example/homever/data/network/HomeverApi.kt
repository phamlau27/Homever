package com.example.homever.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

interface HomeverApi {
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

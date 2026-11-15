package com.example.homever.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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

    @POST("users/me/avatar/presigned-url")
    suspend fun getAvatarPresignedUrl(@Body request: PresignedUrlRequest): Response<PresignedUrlResponse>
}
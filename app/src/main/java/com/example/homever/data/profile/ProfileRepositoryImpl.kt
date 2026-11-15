package com.example.homever.data.profile

import android.net.Uri
import com.example.homever.data.network.HomeverApi
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val api: HomeverApi
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMyProfile()
            when {
                response.isSuccessful -> {
                    Result.success(response.body() ?: UserProfile())
                }
                response.code() == 401 -> Result.failure(Exception("UNAUTHORIZED"))
                // 404 from /users/me always means "no profile yet" for this authenticated user.
                response.code() == 404 -> Result.failure(Exception("PROFILE_NOT_FOUND"))
                else -> Result.failure(Exception("SERVER_ERROR:${response.code()}"))
            }
        } catch (e: Exception) {
            // Network / IO failures — do NOT clear the session for these.
            Result.failure(Exception("NETWORK_ERROR: ${e.message}", e))
        }
    }

    override suspend fun updateMyProfile(profile: UserProfile): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateMyProfile(profile)
            when {
                response.isSuccessful -> Result.success(response.body() ?: profile)
                response.code() == 401 -> Result.failure(Exception("UNAUTHORIZED"))
                else -> Result.failure(Exception("Failed to update profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("NETWORK_ERROR: ${e.message}", e))
        }
    }

    // S3 avatar upload is deferred — this method is a stub until S3 is provisioned.
    override suspend fun uploadAvatar(uri: Uri, contentType: String): Result<String> {
        return Result.failure(Exception("AVATAR_UPLOAD_NOT_CONFIGURED"))
    }
}

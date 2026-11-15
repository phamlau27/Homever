package com.example.homever.data.profile

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.homever.data.network.HomeverApi
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepositoryImpl(
    private val api: HomeverApi,
    private val context: Context? = null
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

    override suspend fun uploadAvatar(uri: Uri, contentType: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val ctx = context ?: return@withContext Result.failure(Exception("Context is required for image upload"))
            val contentResolver = ctx.contentResolver

            val rawType = contentResolver.getType(uri) ?: contentType.ifBlank { "image/jpeg" }
            val resolvedType = when {
                rawType.contains("png", ignoreCase = true) -> "image/png"
                rawType.contains("webp", ignoreCase = true) -> "image/webp"
                else -> "image/jpeg"
            }
            val extension = when (resolvedType) {
                "image/png" -> "png"
                "image/webp" -> "webp"
                else -> "jpg"
            }
            val fileName = "avatar_${System.currentTimeMillis()}.$extension"

            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return@withContext Result.failure(Exception("Could not read image data from URI"))

            val requestBody = bytes.toRequestBody(resolvedType.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", fileName, requestBody)

            val response = api.uploadImage(part)
            if (response.isSuccessful && response.body() != null) {
                val uploadResponse = response.body()!!
                Result.success(uploadResponse.url)
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception("Upload failed with HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("NETWORK_ERROR: ${e.message}", e))
        }
    }
}

package com.example.homever.data.profile

import android.content.Context
import android.net.Uri
import com.example.homever.data.network.HomeverApi
import com.example.homever.data.network.PresignedUrlRequest
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepositoryImpl(
    private val api: HomeverApi,
    private val context: Context
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMyProfile()
            if (response.isSuccessful) {
                Result.success(response.body() ?: UserProfile())
            } else {
                Result.failure(Exception("Failed to get profile: \${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMyProfile(profile: UserProfile): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateMyProfile(profile)
            if (response.isSuccessful) {
                Result.success(response.body() ?: profile)
            } else {
                Result.failure(Exception("Failed to update profile: \${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAvatar(uri: Uri, contentType: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            // 1. Get Presigned URL
            val request = PresignedUrlRequest("avatar_${System.currentTimeMillis()}.jpg", contentType)
            val presignedResponse = api.getAvatarPresignedUrl(request)
            
            if (!presignedResponse.isSuccessful || presignedResponse.body() == null) {
                return@withContext Result.failure(Exception("Failed to get presigned URL"))
            }
            
            val uploadUrl = presignedResponse.body()!!.uploadUrl
            val fileKey = presignedResponse.body()!!.fileKey

            // 2. Read file bytes
            val bytes = context.contentResolver.openInputStream(uri)?.readBytes()
                ?: return@withContext Result.failure(Exception("Failed to read image file"))

            // 3. Upload to S3 using standard OkHttp (No interceptor needed for AWS S3 direct upload)
            val client = OkHttpClient()
            val reqBody = bytes.toRequestBody(contentType.toMediaTypeOrNull())
            val s3Request = Request.Builder()
                .url(uploadUrl)
                .put(reqBody)
                .build()

            val s3Response = client.newCall(s3Request).execute()
            
            if (s3Response.isSuccessful) {
                // Usually the backend returns a CloudFront URL or similar based on fileKey
                Result.success(fileKey) 
            } else {
                Result.failure(Exception("Failed to upload image to S3: \${s3Response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
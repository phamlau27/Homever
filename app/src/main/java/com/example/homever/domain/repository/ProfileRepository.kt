package com.example.homever.domain.repository

import android.net.Uri
import com.example.homever.data.network.UserProfile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<UserProfile>
    suspend fun updateMyProfile(profile: UserProfile): Result<UserProfile>
    suspend fun uploadAvatar(uri: Uri, contentType: String): Result<String> // returns URL or Key
}
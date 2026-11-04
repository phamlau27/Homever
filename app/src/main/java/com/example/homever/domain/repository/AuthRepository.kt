package com.example.homever.domain.repository

interface AuthRepository {
    suspend fun signUp(emailOrPhone: String): Result<Unit>
    suspend fun confirmSignUp(emailOrPhone: String, otp: String): Result<Unit>
    suspend fun signIn(emailOrPhone: String): Result<Boolean> // returns true if OTP required (CUSTOM_CHALLENGE)
    suspend fun confirmSignIn(otp: String): Result<Unit>
    suspend fun signOut()
    fun getAccessToken(): String?
    fun isLoggedIn(): Boolean
}
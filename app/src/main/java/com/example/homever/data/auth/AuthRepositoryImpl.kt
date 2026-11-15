package com.example.homever.data.auth

import com.example.homever.data.session.SessionManager
import com.example.homever.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun signUp(emailOrPhone: String): Result<Unit> {
        return Result.failure(Exception("Native sign-up is not supported. Use Cognito Managed Login via AuthManager."))
    }

    override suspend fun confirmSignUp(emailOrPhone: String, otp: String): Result<Unit> {
        return Result.failure(Exception("Native confirm sign-up is not supported."))
    }

    override suspend fun signIn(emailOrPhone: String): Result<Boolean> {
        return Result.failure(Exception("Native sign-in is not supported. Use Cognito Managed Login via AuthManager."))
    }

    override suspend fun confirmSignIn(otp: String): Result<Unit> {
        return Result.failure(Exception("Native OTP confirmation is not supported."))
    }

    override suspend fun signOut() {
        sessionManager.clearSession()
    }

    override fun getAccessToken(): String? = sessionManager.getAccessToken()

    override fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
}
package com.example.homever.data.auth

import aws.sdk.kotlin.services.cognitoidentityprovider.CognitoIdentityProviderClient
import aws.sdk.kotlin.services.cognitoidentityprovider.model.*
import com.example.homever.data.session.SessionManager
import com.example.homever.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.security.SecureRandom
import android.util.Base64

class AuthRepositoryImpl(
    private val sessionManager: SessionManager
) : AuthRepository {

    // You should properly inject this via DI (e.g. Hilt/Dagger) in a real production app.
    private val clientId = "6r9ajmeb3b63e9j4qhvvfcm82n"
    private var currentSession: String? = null // For CUSTOM_CHALLENGE session

    private fun generateSecurePassword(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP) + "A1!" // Ensure complexity requirements
    }

    override suspend fun signUp(emailOrPhone: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            CognitoIdentityProviderClient { region = "ap-southeast-1" }.use { cognito ->
                val request = SignUpRequest {
                    clientId = this@AuthRepositoryImpl.clientId
                    username = emailOrPhone
                    password = generateSecurePassword() // Secure random password for passwordless users
                }
                cognito.signUp(request)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmSignUp(emailOrPhone: String, otp: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            CognitoIdentityProviderClient { region = "ap-southeast-1" }.use { cognito ->
                val request = ConfirmSignUpRequest {
                    clientId = this@AuthRepositoryImpl.clientId
                    username = emailOrPhone
                    confirmationCode = otp
                }
                cognito.confirmSignUp(request)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(emailOrPhone: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            CognitoIdentityProviderClient { region = "ap-southeast-1" }.use { cognito ->
                val request = InitiateAuthRequest {
                    authFlow = AuthFlowType.fromValue("USER_AUTH") // User Auth for built-in passwordless
                    clientId = this@AuthRepositoryImpl.clientId
                    authParameters = mapOf(
                        "USERNAME" to emailOrPhone,
                        "PREFERRED_CHALLENGE" to "EMAIL_OTP"
                    )
                }
                val response = cognito.initiateAuth(request)
                
                when (response.challengeName) {
                    ChallengeNameType.fromValue("SELECT_CHALLENGE"),
                    ChallengeNameType.fromValue("EMAIL_OTP"),
                    ChallengeNameType.fromValue("SMS_OTP"),
                    ChallengeNameType.CustomChallenge -> {
                        currentSession = response.session
                        // We also need to store the actual username to respond to the challenge later
                        sessionManager.savePendingUsername(emailOrPhone)
                        Result.success(true) // Requires OTP
                    }
                    else -> {
                        if (response.authenticationResult != null) {
                            saveTokens(response.authenticationResult)
                            Result.success(false) // Direct login
                        } else {
                            Result.failure(Exception("Unknown authentication state: \${response.challengeName}"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmSignIn(otp: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val session = currentSession ?: throw IllegalStateException("No active auth session")
            val pendingUsername = sessionManager.getPendingUsername() ?: throw IllegalStateException("No pending username")

            CognitoIdentityProviderClient { region = "ap-southeast-1" }.use { cognito ->
                val request = RespondToAuthChallengeRequest {
                    clientId = this@AuthRepositoryImpl.clientId
                    challengeName = ChallengeNameType.fromValue("EMAIL_OTP") // or SmsOtp depending on what was actually required
                    this.session = session
                    challengeResponses = mapOf(
                        "USERNAME" to pendingUsername,
                        "EMAIL_OTP_CODE" to otp
                    )
                }
                val response = cognito.respondToAuthChallenge(request)
                
                if (response.authenticationResult != null) {
                    saveTokens(response.authenticationResult)
                    currentSession = null
                    sessionManager.clearPendingUsername()
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("OTP Verification failed"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        sessionManager.clearSession()
        currentSession = null
        // Additional token revocation via Cognito if required
    }

    override fun getAccessToken(): String? = sessionManager.getAccessToken()

    override fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    private fun saveTokens(authResult: AuthenticationResultType?) {
        authResult?.let {
            sessionManager.saveTokens(
                accessToken = it.accessToken ?: "",
                idToken = it.idToken ?: "",
                refreshToken = it.refreshToken
            )
        }
    }
}
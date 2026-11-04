package com.example.homever.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import net.openid.appauth.*

class CognitoAuthManager(private val context: Context) {

    private var authService: AuthorizationService = AuthorizationService(context)
    private var authState: AuthState = AuthState()
    private var serviceConfig: AuthorizationServiceConfiguration? = null
    
    // AWS Cognito User Pool details
    private val ISSUER_URI = "https://cognito-idp.ap-southeast-1.amazonaws.com/ap-southeast-1_xCmijwwpz"
    private val CLIENT_ID = "6r9ajmeb3b63e9j4qhvvfcm82n" // Replace with your actual App Client ID
    private val REDIRECT_URI = "com.homever.app://oauth2redirect" // Ensure this is configured in Cognito Allowed Callback URLs
    private val LOGOUT_URI = "homever://logout" // Ensure this is configured in Cognito Allowed Sign Out URLs
    
    init {
        fetchConfiguration()
    }

    private fun fetchConfiguration() {
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(ISSUER_URI)
        ) { config, ex ->
            if (ex != null) {
                Log.e("CognitoAuth", "Failed to fetch configuration", ex)
                return@fetchFromIssuer
            }
            serviceConfig = config
            if (config != null) {
                authState = AuthState(config)
            } else {
                authState = AuthState()
            }
            Log.d("CognitoAuth", "Configuration fetched successfully")
        }
    }

    fun startLogin(launcher: ActivityResultLauncher<Intent>, identityProvider: String? = null) {
        val config = serviceConfig ?: return
        
        val builder = AuthorizationRequest.Builder(
            config,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(REDIRECT_URI)
        ).setScope("openid email")
        
        if (identityProvider != null) {
            builder.setAdditionalParameters(mapOf("identity_provider" to identityProvider))
        }

        val authIntent = authService.getAuthorizationRequestIntent(builder.build())
        launcher.launch(authIntent)
    }

    fun handleLoginResult(intent: Intent, onComplete: (Boolean) -> Unit) {
        val response = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        
        if (response != null) {
            authState.update(response, ex)
            
            // Exchange code for tokens
            authService.performTokenRequest(
                response.createTokenExchangeRequest()
            ) { tokenResponse, tokenEx ->
                authState.update(tokenResponse, tokenEx)
                if (tokenResponse != null) {
                    Log.d("CognitoAuth", "Token exchange succeeded. ID Token: \${tokenResponse.idToken}")
                    onComplete(true)
                } else {
                    Log.e("CognitoAuth", "Token exchange failed", tokenEx)
                    onComplete(false)
                }
            }
        } else {
            Log.e("CognitoAuth", "Authorization failed", ex)
            onComplete(false)
        }
    }
    
    fun startLogout(launcher: ActivityResultLauncher<Intent>) {
        val config = serviceConfig ?: return
        val idToken = authState.idToken ?: return
        
        val endSessionRequest = EndSessionRequest.Builder(config)
            .setIdTokenHint(idToken)
            .setPostLogoutRedirectUri(Uri.parse(LOGOUT_URI))
            .setAdditionalParameters(
                mapOf(
                    "client_id" to CLIENT_ID,
                    "logout_uri" to LOGOUT_URI
                )
            )
            .build()
            
        val endSessionIntent = authService.getEndSessionRequestIntent(endSessionRequest)
        launcher.launch(endSessionIntent)
    }
    
    fun handleLogoutResult(intent: Intent, onComplete: (Boolean) -> Unit) {
        val response = EndSessionResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        
        if (ex == null) {
            // Logout successful
            if (serviceConfig != null) {
                authState = AuthState(serviceConfig!!)
            } else {
                authState = AuthState()
            } // Clear auth state
            onComplete(true)
        } else {
            Log.e("CognitoAuth", "Logout failed", ex)
            onComplete(false)
        }
    }
    
    fun isAuthorized(): Boolean {
        return authState.isAuthorized
    }
    
    fun getAccessToken(): String? {
        return authState.accessToken
    }

    fun dispose() {
        authService.dispose()
    }
}
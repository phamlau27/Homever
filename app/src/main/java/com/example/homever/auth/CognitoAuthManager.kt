package com.example.homever.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.*
import org.json.JSONException

class CognitoAuthManager(private val context: Context) {

    private var authService: AuthorizationService = AuthorizationService(context)
    private var authState: AuthState = AuthState()
    private var serviceConfig: AuthorizationServiceConfiguration? = null

    // Emits true once the OIDC discovery document has been fetched and login is possible.
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    // AWS Cognito User Pool details
    private val ISSUER_URI = "https://cognito-idp.ap-southeast-1.amazonaws.com/ap-southeast-1_xCmijwwpz"
    private val CLIENT_ID = "6r9ajmeb3b63e9j4qhvvfcm82n"
    // Both URIs use the same scheme already registered in AndroidManifest.xml.
    private val REDIRECT_URI = "com.homever.app://oauth2redirect"
    private val LOGOUT_URI = "com.homever.app://logout"

    init {
        restoreAuthState()
        if (authState.authorizationServiceConfiguration != null) {
            serviceConfig = authState.authorizationServiceConfiguration
            _isReady.value = true
            Log.d("CognitoAuth", "Configuration restored from AuthState")
        }
        fetchConfiguration()
    }

    private fun persistAuthState() {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("stateJson", authState.jsonSerializeString()).apply()
    }

    private fun restoreAuthState() {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val stateJson = prefs.getString("stateJson", null)
        if (stateJson != null) {
            try {
                authState = AuthState.jsonDeserialize(stateJson)
            } catch (ex: JSONException) {
                Log.e("CognitoAuth", "Failed to deserialize auth state", ex)
                authState = AuthState()
            }
        }
    }

    private fun fetchConfiguration() {
        AuthorizationServiceConfiguration.fetchFromIssuer(
            Uri.parse(ISSUER_URI)
        ) { config, ex ->
            if (ex != null) {
                Log.e("CognitoAuth", "Failed to fetch configuration from issuer", ex)
                return@fetchFromIssuer
            }
            if (config != null) {
                serviceConfig = config
                if (authState.authorizationServiceConfiguration == null) {
                    authState = AuthState(config)
                    persistAuthState()
                }
                _isReady.value = true
                Log.d("CognitoAuth", "Configuration fetched successfully: authEndpoint=${config.authorizationEndpoint}")
            }
        }
    }

    fun startLogin(launcher: ActivityResultLauncher<Intent>, identityProvider: String? = null) {
        val config = serviceConfig
        if (config == null) {
            Log.w("CognitoAuth", "startLogin called before serviceConfig is ready")
            return
        }
        
        val builder = AuthorizationRequest.Builder(
            config,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(REDIRECT_URI)
        ).setScope("openid email")
        
        if (identityProvider != null) {
            builder.setAdditionalParameters(mapOf("identity_provider" to identityProvider))
        }

        try {
            val authIntent = authService.getAuthorizationRequestIntent(builder.build())
            launcher.launch(authIntent)
        } catch (ex: Exception) {
            Log.e("CognitoAuth", "Failed to launch authorization intent", ex)
        }
    }

    fun startLogin(launcher: ActivityResultLauncher<Intent>) {
        startLogin(launcher, null)
    }

    fun handleLoginResult(intent: Intent, onComplete: (success: Boolean, isCancelled: Boolean) -> Unit) {
        val response = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        
        if (response != null) {
            authState.update(response, ex)
            persistAuthState()
            
            // Exchange code for tokens
            authService.performTokenRequest(
                response.createTokenExchangeRequest()
            ) { tokenResponse, tokenEx ->
                authState.update(tokenResponse, tokenEx)
                persistAuthState()
                
                if (tokenResponse != null) {
                    Log.d("CognitoAuth", "Token exchange succeeded.")
                    onComplete(true, false)
                } else {
                    Log.e("CognitoAuth", "Token exchange failed", tokenEx)
                    onComplete(false, false)
                }
            }
        } else {
            val isCancelled = ex?.type == 0 && ex.code == 1
            if (isCancelled) {
                Log.d("CognitoAuth", "User cancelled authorization flow")
            } else {
                Log.e("CognitoAuth", "Authorization failed", ex)
            }
            onComplete(false, isCancelled)
        }
    }
    
    /**
     * Initiates federated logout via Cognito Hosted UI.
     * Falls back to local-only logout when the id_token is unavailable (e.g. after a silent
     * token refresh that did not return a new id_token) by invoking [onLocalFallback].
     */
    fun startLogout(
        launcher: ActivityResultLauncher<Intent>,
        onLocalFallback: () -> Unit = {}
    ) {
        val config = serviceConfig
        val idToken = authState.idToken

        if (config == null || idToken == null) {
            // Cannot build an end-session request — clear state locally and notify caller.
            Log.w("CognitoAuth", "startLogout: config=$config idToken=$idToken — performing local logout only")
            clearAuthState()
            onLocalFallback()
            return
        }

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

        try {
            val endSessionIntent = authService.getEndSessionRequestIntent(endSessionRequest)
            launcher.launch(endSessionIntent)
        } catch (ex: Exception) {
            Log.e("CognitoAuth", "Failed to launch logout intent", ex)
            clearAuthState()
            onLocalFallback()
        }
    }
    
    fun handleLogoutResult(intent: Intent, onComplete: (Boolean) -> Unit) {
        val response = EndSessionResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        
        if (ex == null) {
            clearAuthState()
            onComplete(true)
        } else {
            Log.e("CognitoAuth", "Logout failed", ex)
            clearAuthState()
            onComplete(false)
        }
    }
    
    fun isAuthorized(): Boolean {
        return authState.isAuthorized
    }
    
    fun getAccessToken(): String? {
        return authState.accessToken
    }

    fun getIdToken(): String? {
        return authState.idToken
    }

    fun getRefreshToken(): String? {
        return authState.refreshToken
    }

    fun getFreshAccessToken(onResult: (accessToken: String?, idToken: String?, refreshToken: String?) -> Unit) {
        if (!authState.isAuthorized) {
            onResult(null, null, null)
            return
        }

        authState.performActionWithFreshTokens(authService) { accessToken, idToken, ex ->
            if (ex != null) {
                Log.e("CognitoAuth", "Failed to refresh token", ex)
                onResult(null, null, null)
            } else {
                persistAuthState()
                onResult(accessToken, idToken, authState.refreshToken)
            }
        }
    }

    fun clearAuthState() {
        if (serviceConfig != null) {
            authState = AuthState(serviceConfig!!)
        } else {
            authState = AuthState()
        }
        persistAuthState()
    }

    fun dispose() {
        authService.dispose()
    }
}
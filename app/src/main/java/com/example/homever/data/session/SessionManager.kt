package com.example.homever.data.session

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {
    private val appContext = context.applicationContext
    
    private val masterKey by lazy {
        MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val sharedPreferences by lazy {
        try {
            EncryptedSharedPreferences.create(
                appContext,
                "homever_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e("SessionManager", "Error initializing EncryptedSharedPreferences", e)
            // Handle Keystore invalidation / corrupted data
            appContext.getSharedPreferences("homever_secure_prefs", Context.MODE_PRIVATE).edit().clear().apply()
            EncryptedSharedPreferences.create(
                appContext,
                "homever_secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun saveTokens(accessToken: String, idToken: String, refreshToken: String?) {
        val editor = sharedPreferences.edit()
            .putString("access_token", accessToken)
            .putString("id_token", idToken)
            
        if (refreshToken != null) {
            editor.putString("refresh_token", refreshToken)
        }
        
        editor.apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)
    
    fun getIdToken(): String? = sharedPreferences.getString("id_token", null)
    
    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)

    fun saveProfileCompleted(completed: Boolean) {
        sharedPreferences.edit().putBoolean("profile_completed", completed).apply()
    }

    fun isProfileCompleted(): Boolean {
        return sharedPreferences.getBoolean("profile_completed", false)
    }

    fun savePendingUsername(username: String) {
        sharedPreferences.edit().putString("pending_username", username).apply()
    }
    
    fun getPendingUsername(): String? = sharedPreferences.getString("pending_username", null)
    
    fun clearPendingUsername() {
        sharedPreferences.edit().remove("pending_username").apply()
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}
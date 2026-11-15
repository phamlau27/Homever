package com.example.homever.data.network

import com.example.homever.auth.CognitoAuthManager
import com.example.homever.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton that holds the [Retrofit] instance and [HomeverApi].
 *
 * Call [initialize] once from [com.example.homever.MainActivity.onCreate] with the
 * [SessionManager] and [CognitoAuthManager] that have already been created for that Activity
 * lifetime. The singleton pattern prevents the OkHttpClient and Retrofit being recreated on
 * every configuration change.
 *
 * Base URL is intentionally left as a constant so that swapping to a production URL requires a
 * single change here, with no scattered hardcoded strings.
 */
object NetworkModule {

    // Change this to your production base URL before release.
    private const val BASE_URL = "http://192.168.0.104:3000/v1/"

    private var _api: HomeverApi? = null

    val api: HomeverApi
        get() = _api ?: error("NetworkModule not initialized — call initialize() first")

    /**
     * Idempotent initializer. If called more than once with the same arguments the existing
     * instance is reused. If called with different [sessionManager] / [authManager] instances
     * (e.g., after process restart) the client is rebuilt so the new interceptors are used.
     */
    fun initialize(
        sessionManager: SessionManager,
        authManager: CognitoAuthManager
    ): HomeverApi {
        val existing = _api
        if (existing != null) return existing

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(sessionManager, authManager))
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(HomeverApi::class.java).also { _api = it }
    }

    /** Resets the singleton — useful for tests or after a full logout. */
    fun reset() {
        _api = null
    }
}


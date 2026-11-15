package com.example.homever.data.network

import android.util.Log
import com.example.homever.auth.CognitoAuthManager
import com.example.homever.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val cognitoAuthManager: CognitoAuthManager
) : Interceptor {

    // Prevents simultaneous refresh attempts from multiple threads.
    private val isRefreshing = AtomicBoolean(false)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Attach the current access token.
        val accessToken = sessionManager.getAccessToken()
        val requestWithToken = originalRequest.newBuilder().apply {
            if (accessToken != null) {
                header("Authorization", "Bearer $accessToken")
            }
        }.build()

        val response = chain.proceed(requestWithToken)

        // On 401, attempt a silent token refresh and retry once.
        // Do NOT clear the session for network errors (IOException) — only for genuine 401s.
        if (response.code == 401) {
            Log.d("AuthInterceptor", "401 received — attempting token refresh")
            response.close()

            val newAccessToken = refreshTokenSync()
            if (newAccessToken == null) {
                Log.w("AuthInterceptor", "Token refresh returned null — forwarding 401 to caller")
                // Re-execute with the same (expired) token so the caller gets a proper 401
                // response body and can decide whether to clear the session.
                return chain.proceed(requestWithToken)
            }

            Log.d("AuthInterceptor", "Token refreshed — retrying original request")
            val retryRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
            return chain.proceed(retryRequest)
        }

        return response
    }

    /**
     * Calls [CognitoAuthManager.getFreshAccessToken] on a background thread (AppAuth uses its
     * own handler) and blocks the OkHttp thread until it completes or times out.
     *
     * Returns the new access token string on success, null on failure.
     * Prevents concurrent refresh calls via [isRefreshing].
     */
    private fun refreshTokenSync(): String? {
        // If another thread is already refreshing, wait for it and return whatever was saved.
        if (!isRefreshing.compareAndSet(false, true)) {
            Log.d("AuthInterceptor", "Refresh already in progress — waiting")
            Thread.sleep(2000)
            return sessionManager.getAccessToken()
        }

        return try {
            var newToken: String? = null
            val latch = CountDownLatch(1)

            cognitoAuthManager.getFreshAccessToken { accessToken, idToken, refreshToken ->
                if (accessToken != null) {
                    sessionManager.saveTokens(accessToken, idToken ?: "", refreshToken)
                    newToken = accessToken
                    Log.d("AuthInterceptor", "Token refresh succeeded")
                } else {
                    Log.w("AuthInterceptor", "getFreshAccessToken returned null tokens")
                }
                latch.countDown()
            }

            // Wait up to 15 s for the refresh to complete.
            val completed = latch.await(15, TimeUnit.SECONDS)
            if (!completed) {
                Log.w("AuthInterceptor", "Token refresh timed out after 15 s")
            }
            newToken
        } finally {
            isRefreshing.set(false)
        }
    }
}

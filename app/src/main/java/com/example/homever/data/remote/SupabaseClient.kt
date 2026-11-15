package com.example.homever.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    const val SUPABASE_URL = "https://katvxqeqnpodlgqdxxrn.supabase.co"
    const val SUPABASE_KEY = "sb_publishable_yt9TaEKCkGsDiNA2ghLguA_eeKZFotA"

    // Web Client ID từ Google Cloud Console (Dùng cho Google Sign-In Credential Manager)
    // Bạn có thể cung cấp Web Client ID ở đây hoặc dùng OAuth qua trình duyệt mặc định.
    const val GOOGLE_WEB_CLIENT_ID = ""

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth) {
            scheme = "homever"
            host = "login-callback"
        }
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}

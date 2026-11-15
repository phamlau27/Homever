package com.example.homever.data.repository

import android.content.Context
import android.util.Patterns
import com.example.homever.data.model.UserProfile
import com.example.homever.data.remote.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.exception.AuthWeakPasswordException
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class AuthRepository {
    private val auth = SupabaseClient.client.auth
    private val db = SupabaseClient.client

    // Lấy ID người dùng đang đăng nhập
    fun getCurrentUserId(): String? {
        return auth.currentUserOrNull()?.id
    }

    // Lấy email người dùng đang đăng nhập
    fun getCurrentUserEmail(): String? {
        return auth.currentUserOrNull()?.email
    }

    // Kiểm tra xem phiên làm việc hiện tại còn hiệu lực hay không
    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        try {
            val session = auth.currentSessionOrNull()
            if (session != null) {
                return@withContext true
            }
            // Thử tải phiên từ Bộ nhớ máy (Storage)
            return@withContext auth.loadFromStorage()
        } catch (e: Exception) {
            false
        }
    }

    // Kiểm tra tính hợp lệ của Email
    fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        return trimmed.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()
    }

    // Kiểm tra tính hợp lệ của Mật khẩu (tối thiểu 6 ký tự)
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    // Đăng ký tài khoản mới bằng Email/Mật khẩu
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val cleanEmail = email.trim()
            require(isValidEmail(cleanEmail)) { "Địa chỉ email không đúng định dạng." }
            require(isValidPassword(password)) { "Mật khẩu phải chứa ít nhất 6 ký tự." }

            auth.signUpWith(Email) {
                this.email = cleanEmail
                this.password = password
            }
            Unit
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Đăng nhập bằng Email/Mật khẩu
    suspend fun signInWithEmail(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val cleanEmail = email.trim()
            require(isValidEmail(cleanEmail)) { "Địa chỉ email không đúng định dạng." }
            require(password.isNotBlank()) { "Vui lòng nhập mật khẩu." }

            auth.signInWith(Email) {
                this.email = cleanEmail
                this.password = password
            }
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Đăng nhập bằng Google ID Token (Nếu ứng dụng nhận ID token từ Google Sign-In / CredentialManager)
    suspend fun signInWithGoogleIdToken(idToken: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            require(idToken.isNotBlank()) { "Mã xác thực Google ID token không hợp lệ." }

            auth.signInWith(IDToken) {
                this.idToken = idToken
                this.provider = Google
            }
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Đăng nhập bằng Google OAuth (Mở trình duyệt / Custom Tabs)
    suspend fun signInWithGoogleOAuth(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            auth.signInWith(Google)
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Quên mật khẩu: Gửi email đặt lại mật khẩu
    suspend fun resetPasswordForEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val cleanEmail = email.trim()
            require(isValidEmail(cleanEmail)) { "Địa chỉ email không đúng định dạng." }

            auth.resetPasswordForEmail(
                email = cleanEmail,
                redirectUrl = "homever://reset-password"
            )
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Gửi lại Email xác nhận tài khoản
    suspend fun resendVerificationEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val cleanEmail = email.trim()
            require(isValidEmail(cleanEmail)) { "Địa chỉ email không đúng định dạng." }

            auth.resendEmail(OtpType.Email.SIGNUP, cleanEmail)
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Đổi mật khẩu mới cho tài khoản đang đăng nhập
    suspend fun updatePassword(newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            require(isValidPassword(newPassword)) { "Mật khẩu mới phải chứa ít nhất 6 ký tự." }

            auth.updateUser {
                password = newPassword
            }
            Unit
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Đăng xuất và xóa phiên làm việc
    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            auth.signOut()
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Lưu hoặc cập nhật hồ sơ người dùng vào bảng "profiles"
    suspend fun upsertProfile(profile: UserProfile): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            db.from("profiles").upsert(profile)
            Unit
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Lấy thông tin hồ sơ của người dùng
    suspend fun getProfile(userId: String): Result<UserProfile?> = withContext(Dispatchers.IO) {
        runCatching {
            db.from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<UserProfile>()
        }.mapFailure { Throwable(mapAuthError(it)) }
    }

    // Phân tích và chuyển đổi các ngoại lệ Supabase / Mạng sang thông báo tiếng Việt rõ ràng
    private fun mapAuthError(throwable: Throwable): String {
        return when (throwable) {
            is IllegalArgumentException -> throwable.message ?: "Dữ liệu nhập không hợp lệ."
            is AuthWeakPasswordException -> "Mật khẩu quá yếu. Vui lòng nhập mật khẩu tối thiểu 6 ký tự phức tạp hơn."
            is AuthRestException -> {
                val msg = throwable.message.orEmpty().lowercase()
                when {
                    msg.contains("invalid login credentials") || msg.contains("invalid_credentials") ->
                        "Email hoặc mật khẩu không chính xác."
                    msg.contains("user already registered") || msg.contains("already registered") ->
                        "Email này đã được đăng ký tài khoản. Vui lòng chọn Đăng nhập."
                    msg.contains("email not confirmed") ->
                        "Email chưa được xác nhận. Vui lòng kiểm tra hộp thư email của bạn để xác thực."
                    msg.contains("rate limit") || msg.contains("over_email_send_rate_limit") ->
                        "Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau ít phút."
                    msg.contains("token expired") || msg.contains("token has expired") ->
                        "Liên kết hoặc mã xác thực đã hết hạn. Vui lòng gửi lại yêu cầu mới."
                    else -> throwable.message ?: "Lỗi xác thực từ máy chủ."
                }
            }
            is RestException -> {
                val msg = throwable.message.orEmpty().lowercase()
                when {
                    msg.contains("invalid login credentials") -> "Email hoặc mật khẩu không chính xác."
                    msg.contains("already registered") -> "Email này đã được đăng ký."
                    else -> throwable.message ?: "Lỗi phản hồi từ máy chủ Supabase (${throwable.statusCode})."
                }
            }
            is HttpRequestTimeoutException -> "Kết nối quá thời hạn (Timeout). Vui lòng kiểm tra lại mạng internet."
            is HttpRequestException, is UnknownHostException, is IOException ->
                "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại mạng Wi-Fi hoặc 4G/5G của bạn."
            else -> throwable.message ?: "Đã xảy ra lỗi không xác định. Vui lòng thử lại sau."
        }
    }
}

// Inline extension helper cho runCatching mapping
private inline fun <T> Result<T>.mapFailure(transform: (Throwable) -> Throwable): Result<T> {
    return fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(transform(it)) }
    )
}

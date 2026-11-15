package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.data.repository.AuthRepository
import com.example.homever.ui.theme.*
import kotlinx.coroutines.launch

private val ForgotArrowBack: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ForgotArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Quên mật khẩu",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Lấy lại mật khẩu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nhập email đã đăng ký của bạn. Chúng tôi sẽ gửi đường dẫn đặt lại mật khẩu về hòm thư của bạn.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Email của bạn",
            fontSize = 13.sp,
            color = TextGray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                message = null
            },
            placeholder = {
                Text(
                    text = "Nhập email của bạn...",
                    color = Color(0xFFA0AAB0),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = BluePrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true
        )

        if (message != null) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = message ?: "",
                color = if (isSuccess) Color(0xFF16A34A) else Color.Red,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val cleanEmail = email.trim()
                if (!authRepository.isValidEmail(cleanEmail)) {
                    message = "Vui lòng nhập địa chỉ email hợp lệ."
                    isSuccess = false
                    return@Button
                }
                isLoading = true
                message = null
                scope.launch {
                    val result = authRepository.resetPasswordForEmail(cleanEmail)
                    isLoading = false
                    result.fold(
                        onSuccess = {
                            isSuccess = true
                            message = "Đã gửi liên kết đặt lại mật khẩu! Vui lòng kiểm tra hộp thư email của bạn."
                        },
                        onFailure = { error ->
                            isSuccess = false
                            message = error.message ?: "Không thể gửi email. Vui lòng thử lại sau."
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Gửi yêu cầu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    HomEverTheme {
        ForgotPasswordScreen()
    }
}

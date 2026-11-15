package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*

// ── Google "G" icon (simplified coloured ring) ─────────────────────────────
val GoogleIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Google",
        defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        // Outer circle outline
        path(stroke = SolidColor(Color(0xFF4285F4)), strokeLineWidth = 2f) {
            moveTo(12f, 22f)
            arcTo(10f, 10f, 0f, true, false, 12f, 2f)
            arcTo(10f, 10f, 0f, true, false, 12f, 22f)
            close()
        }
        // "G" cross-bar
        path(stroke = SolidColor(Color(0xFF4285F4)), strokeLineWidth = 2f) {
            moveTo(17f, 12f); lineTo(12f, 12f); lineTo(12f, 17f)
        }
        path(stroke = SolidColor(Color(0xFF4285F4)), strokeLineWidth = 2f) {
            moveTo(12f, 12f); lineTo(7.5f, 12f)
        }
    }.build()

// ── Homever logo "H" mark ──────────────────────────────────────────────────
private val LoginLogoIcon: ImageVector
    get() = ImageVector.Builder(
        name = "HomeverLogo",
        defaultWidth = 32.dp, defaultHeight = 32.dp,
        viewportWidth = 32f, viewportHeight = 32f
    ).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(6f, 4f); lineTo(6f, 28f); lineTo(10f, 28f); lineTo(10f, 18f)
            lineTo(22f, 18f); lineTo(22f, 28f); lineTo(26f, 28f); lineTo(26f, 4f)
            lineTo(22f, 4f); lineTo(22f, 14f); lineTo(10f, 14f); lineTo(10f, 4f)
            close()
        }
    }.build()

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isAuthReady: Boolean = true,
    errorMessage: String? = null,
    onContinueClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // ── Background gradient ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(
                    Brush.verticalGradient(
                        listOf(HomeverBlueDark, HomeverBlue)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .align(Alignment.BottomCenter)
                .background(HomeverGray)
        )

        // ── Content ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Hero section ───────────────────────────────────────────────
            Spacer(Modifier.height(48.dp))

            // Logo badge
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = LoginLogoIcon,
                    contentDescription = "Homever",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Homever",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Tìm bạn cùng phòng lý tưởng\nvà không gian sống như ở nhà.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(44.dp))

            // ── Card ──────────────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Chào mừng bạn 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomeverBlueDark
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Đăng nhập hoặc tạo tài khoản để bắt đầu",
                        fontSize = 13.sp,
                        color = HomeverTextGray,
                        textAlign = TextAlign.Center
                    )

                    // ── Error banner ───────────────────────────────────────
                    if (errorMessage != null) {
                        Spacer(Modifier.height(16.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = HomeverRedLight,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = errorMessage,
                                color = HomeverRed,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── Primary CTA: Cognito Hosted UI (email / phone) ─────
                    Button(
                        onClick = onContinueClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HomeverBlue,
                            disabledContainerColor = HomeverBlue.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !isLoading && isAuthReady
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else if (!isAuthReady) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Đăng nhập / Đăng ký",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // ── Divider ────────────────────────────────────────────
                    Spacer(Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = HomeverGrayBorder
                        )
                        Text(
                            text = "  hoặc  ",
                            fontSize = 12.sp,
                            color = HomeverTextGray
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = HomeverGrayBorder
                        )
                    }
                    Spacer(Modifier.height(20.dp))

                    // ── Google Sign-In ─────────────────────────────────────
                    OutlinedButton(
                        onClick = onGoogleLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = HomeverTextDark
                        ),
                        enabled = !isLoading && isAuthReady
                    ) {
                        Icon(
                            imageVector = GoogleIcon,
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Tiếp tục với Google",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // ── Init hint ──────────────────────────────────────────
                    if (!isAuthReady) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Đang kết nối tới máy chủ xác thực…",
                            fontSize = 11.sp,
                            color = HomeverTextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Footer ─────────────────────────────────────────────────────
            Text(
                text = "Bằng cách tiếp tục, bạn đồng ý với\nĐiều khoản dịch vụ và Chính sách bảo mật.",
                fontSize = 11.sp,
                color = HomeverTextGray,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    com.example.homever.ui.theme.HomEverTheme {
        LoginScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    com.example.homever.ui.theme.HomEverTheme {
        LoginScreen(isLoading = true)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenNotReadyPreview() {
    com.example.homever.ui.theme.HomEverTheme {
        LoginScreen(isAuthReady = false)
    }
}

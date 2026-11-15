package com.example.homever.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme
import kotlinx.coroutines.delay

val SplashBlue = Color(0xFF2563EB) // Màu nền #2563EB từ XML

val HomeverLogoIcon: ImageVector
    get() = ImageVector.Builder(
        name = "HomeverLogo",
        defaultWidth = 64.dp,
        defaultHeight = 64.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        // Ngôi nhà cách điệu kết hợp kết nối
        path(fill = SolidColor(Color.White)) {
            moveTo(10f, 20f)
            lineTo(14f, 20f)
            lineTo(14f, 14f)
            lineTo(10f, 14f)
            close()
            moveTo(21f, 10.27f)
            lineTo(12f, 3f)
            lineTo(3f, 10.27f)
            lineTo(3f, 21f)
            lineTo(8f, 21f)
            lineTo(8f, 12f)
            lineTo(16f, 12f)
            lineTo(16f, 21f)
            lineTo(21f, 21f)
            close()
        }
    }.build()

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {}
) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Hiệu ứng fade in & scale nhẹ
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing))
        delay(1200) // Dừng một chút để hiển thị thương hiệu
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SplashBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Icon Logo trắng
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = HomeverLogoIcon,
                    contentDescription = "HomEver Logo",
                    tint = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tên ứng dụng
            Text(
                text = "Homever",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Tìm bạn ở ghép & phòng trọ lý tưởng",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    HomEverTheme {
        SplashScreen()
    }
}

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

val SplashBlue = Color(0xFF3B66F5)

// Logo chính xác theo thiết kế Figma 01-splash: Mái nhà + Trái tim lồng ghép kết nối
val HomeverSplashLogoIcon: ImageVector
    get() = ImageVector.Builder(
        name = "HomeverSplashLogo",
        defaultWidth = 100.dp,
        defaultHeight = 100.dp,
        viewportWidth = 100f,
        viewportHeight = 100f
    ).apply {
        // Mái nhà che chở
        path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 6f
        ) {
            moveTo(18f, 50f)
            lineTo(50f, 22f)
            lineTo(82f, 50f)
        }
        // Thân nhà & Trái tim lồng ghép
        path(
            stroke = SolidColor(Color.White),
            strokeLineWidth = 6f
        ) {
            moveTo(28f, 48f)
            lineTo(28f, 76f)
            lineTo(72f, 76f)
            lineTo(72f, 48f)
        }
        // Biểu tượng trái tim / kết nối bên trong
        path(
            fill = SolidColor(Color.White)
        ) {
            moveTo(50f, 68f)
            curveTo(46f, 64f, 36f, 55f, 36f, 46f)
            curveTo(36f, 40f, 41f, 36f, 46.5f, 36f)
            curveTo(49.5f, 36f, 52.5f, 38f, 50f, 42f)
            curveTo(47.5f, 38f, 50.5f, 36f, 53.5f, 36f)
            curveTo(59f, 36f, 64f, 40f, 64f, 46f)
            curveTo(64f, 55f, 54f, 64f, 50f, 68f)
            close()
        }
    }.build()

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashFinished: () -> Unit = {}
) {
    val scale = remember { Animatable(0.75f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing))
        delay(1300)
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
            Icon(
                imageVector = HomeverSplashLogoIcon,
                contentDescription = "Homever Logo",
                tint = Color.White,
                modifier = Modifier.size(108.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Homever",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }

        // Tagline ở đáy màn hình
        Text(
            text = "Hợp ý bạn, chuẩn ý phòng",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    HomEverTheme {
        SplashScreen()
    }
}

package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

val ArrowBackIcon: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

@Composable
fun OtpScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ArrowBackIcon,
                contentDescription = "Back",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Xác thực OTP",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Nhập mã xác thực",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Mã OTP gồm 6 chữ số đã được gửi đến số điện thoại\ncủa bạn.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // OTP Inputs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val otpValues = listOf("5", "2", "8", "•", "", "")
            otpValues.forEachIndexed { index, value ->
                val isActive = index < 4
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = 1.dp,
                            color = if (isActive) BluePrimary else Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TitleBlue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = TextGray)) {
                    append("Chưa nhận được mã? ")
                }
                withStyle(style = SpanStyle(color = BluePrimary, fontWeight = FontWeight.Medium)) {
                    append("Gửi lại (59s)")
                }
            },
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Xác nhận",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    HomEverTheme {
        OtpScreen()
    }
}
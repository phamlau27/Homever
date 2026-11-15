package com.example.homever.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MatchScreen(
    modifier: Modifier = Modifier,
    myName: String = "Bạn",
    myAvatarUrl: String? = null,
    otherName: String = "Bạn cùng phòng",
    otherAvatarUrl: String? = null,
    onStartChat: () -> Unit = {},
    onContinueDiscover: () -> Unit = {}
) {
    val orange = Color(0xFFDF7E32)
    val blue = Color(0xFF3F4ABD)
    Box(
        modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFFFE9D2), Color(0xFFFFFAF4)))
        )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val bursts = listOf(
                Offset(size.width * 0.22f, size.height * 0.52f),
                Offset(size.width * 0.83f, size.height * 0.35f),
                Offset(size.width * 0.51f, size.height * 0.28f)
            )
            bursts.forEachIndexed { index, center ->
                val color = if (index == 1) blue.copy(alpha = 0.12f) else orange.copy(alpha = 0.14f)
                repeat(12) { ray ->
                    val angle = ray * Math.PI / 6
                    val length = size.width * 0.16f
                    val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
                    drawLine(color, center + direction * 15f, center + direction * length, strokeWidth = 3f)
                    drawCircle(color, 5f, center + direction * (length + 14f))
                }
            }
        }
        Column(Modifier.fillMaxSize().safeDrawingPadding().padding(horizontal = 24.dp)) {
            Column(
                Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, orange)) {
                    Text("TUYỆT VỜI!", color = orange, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                Spacer(Modifier.height(24.dp))
                Text("Hai bạn đã tương thích!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = blue, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text("Bạn và $otherName đã cùng quan tâm. Hãy bắt đầu trò chuyện nhé!", textAlign = TextAlign.Center, color = Color(0xFF6B7280), fontSize = 14.sp)
                Spacer(Modifier.height(40.dp))
                Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                    ChatAvatar(myName, myAvatarUrl, Modifier.offset(x = (-44).dp).size(112.dp).border(4.dp, Color.White, CircleShape).padding(4.dp))
                    ChatAvatar(otherName, otherAvatarUrl, Modifier.offset(x = 44.dp).size(112.dp).border(4.dp, Color.White, CircleShape).padding(4.dp))
                }
                Text(otherName, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = blue, textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
            }
            Button(onClick = onStartChat, modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp), colors = ButtonDefaults.buttonColors(containerColor = orange), shape = RoundedCornerShape(28.dp)) {
                Text("Nhắn tin ngay", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onContinueDiscover, modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9FAF0)), shape = RoundedCornerShape(28.dp)) {
                Text("Tiếp tục khám phá", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = blue)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchScreenPreview() {
    HomEverTheme { MatchScreen(otherName = "Minh Anh") }
}

package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

private val CustomOrange = Color(0xFFF97316)
private val CustomBlue = Color(0xFF2962FF)

@Composable
fun MatchScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF0E6), Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            
            // Badge
            Box(
                modifier = Modifier
                    .border(1.dp, CustomOrange, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("TUYỆT VỜI!", color = CustomOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Hai bạn đã tương thích!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CustomBlue)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                "Cả hai đã cùng nhấn quan tâm. Hãy bắt đầu trò\nchuyện nhé!", 
                textAlign = TextAlign.Center, 
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(80.dp))
            
            // Avatars
            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                // Background decoration (simple lines/dots placeholder)
                // Left avatar
                Box(
                    modifier = Modifier
                        .offset(x = (-40).dp)
                        .size(110.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ảnh 1", color = Color.Gray, fontSize = 12.sp)
                }
                // Right avatar
                Box(
                    modifier = Modifier
                        .offset(x = 40.dp)
                        .size(110.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color(0xFFD1D5DB), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ảnh 2", color = Color.Gray, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CustomOrange),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Nhắn tin ngay", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDDF4FF)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Tiếp tục khám phá", color = CustomBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchScreenPreview() {
    HomEverTheme {
        MatchScreen()
    }
}
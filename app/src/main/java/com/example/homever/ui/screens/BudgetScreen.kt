package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

@Composable
fun BudgetScreen(modifier: Modifier = Modifier) {
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
                text = "Ngân sách",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TitleBlue
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Khoảng giá mong muốn",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Chúng tôi sẽ lọc những gợi ý phù hợp với khả năng\nchi trả của bạn.",
            fontSize = 14.sp,
            color = TextGray,
            lineHeight = 20.sp
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Display Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Khoảng ngân sách", color = TextGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("2.0 - 5.0 triệu/tháng", color = BluePrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Mock Slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color(0xFFE5E7EB), CircleShape)
            )
            
            // Active track
            Box(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .width(180.dp)
                    .height(4.dp)
                    .background(BluePrimary, CircleShape)
            )
            
            // Thumbs
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.width(40.dp))
                // Thumb 1
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, BluePrimary, CircleShape)
                )
                Spacer(modifier = Modifier.width(156.dp))
                // Thumb 2
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, BluePrimary, CircleShape)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Min Max labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("1 triệu", color = TextGray, fontSize = 13.sp)
            Text("10 triệu+", color = TextGray, fontSize = 13.sp)
        }
        
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
                text = "Tiếp tục",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetScreenPreview() {
    HomEverTheme {
        BudgetScreen()
    }
}
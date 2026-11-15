package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

val ArrowDownIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            stroke = SolidColor(BluePrimary),
            strokeLineWidth = 2f
        ) {
            moveTo(6f, 9f)
            lineTo(12f, 15f)
            lineTo(18f, 9f)
        }
    }.build()

// Illustration vector mô phỏng 2 bạn trẻ đập tay (High-five) trong phòng khách
val RoommatesIllustrationIcon: ImageVector
    get() = ImageVector.Builder(
        name = "RoommatesIllustration",
        defaultWidth = 160.dp,
        defaultHeight = 160.dp,
        viewportWidth = 100f,
        viewportHeight = 100f
    ).apply {
        // Sofa / Nền phòng
        path(fill = SolidColor(Color(0xFFE2E8F0))) {
            moveTo(15f, 50f)
            lineTo(85f, 50f)
            lineTo(85f, 75f)
            lineTo(15f, 75f)
            close()
        }
        // Thảm phòng
        path(fill = SolidColor(Color(0xFFCBD5E1))) {
            moveTo(10f, 75f)
            lineTo(90f, 75f)
            lineTo(85f, 90f)
            lineTo(15f, 90f)
            close()
        }
        // Người 1 (áo vàng cam)
        path(fill = SolidColor(Color(0xFFFBBF24))) {
            moveTo(30f, 42f)
            lineTo(42f, 42f)
            lineTo(40f, 65f)
            lineTo(32f, 65f)
            close()
        }
        // Đầu người 1
        path(fill = SolidColor(Color(0xFF1E293B))) {
            moveTo(36f, 30f)
            arcTo(6f, 6f, 0f, true, true, 36f, 42f)
            arcTo(6f, 6f, 0f, true, true, 36f, 30f)
            close()
        }
        // Người 2 (áo hồng tím)
        path(fill = SolidColor(Color(0xFFF472B6))) {
            moveTo(58f, 42f)
            lineTo(70f, 42f)
            lineTo(68f, 65f)
            lineTo(60f, 65f)
            close()
        }
        // Đầu người 2
        path(fill = SolidColor(Color(0xFF1E293B))) {
            moveTo(64f, 30f)
            arcTo(6f, 6f, 0f, true, true, 64f, 42f)
            arcTo(6f, 6f, 0f, true, true, 64f, 30f)
            close()
        }
        // Cánh tay chạm nhau (High five)
        path(
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 4f
        ) {
            moveTo(40f, 45f)
            lineTo(50f, 32f)
            lineTo(60f, 45f)
        }
    }.build()

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Language Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ngôn ngữ / Language",
                color = TextGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier
                    .border(1.dp, BluePrimary, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { /* Select Language */ }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiếng Việt",
                    color = BluePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ArrowDownIcon,
                    contentDescription = "Select Language",
                    tint = BluePrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Illustration card mô phỏng 2 bạn trẻ trong phòng khách
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .aspectRatio(1.15f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFDFBF7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = RoommatesIllustrationIcon,
                contentDescription = "Roommates finding",
                modifier = Modifier.fillMaxSize(0.85f),
                tint = Color.Unspecified
            )
        }
        
        Spacer(modifier = Modifier.height(36.dp))
        
        // Title
        Text(
            text = "Tìm bạn ở ghép lý tưởng",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(14.dp))
        
        // Description
        Text(
            text = "Kết nối với những roommate có chung phong cách sống, ngân sách và thói quen sinh hoạt an toàn, văn minh.",
            fontSize = 14.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Page Indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(BluePrimary)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCBD5E1))
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCBD5E1))
            )
        }
        
        Spacer(modifier = Modifier.weight(1.2f))
        
        // Button
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Bắt đầu ngay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    HomEverTheme {
        OnboardingScreen()
    }
}

package com.example.homever.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

private val CustomArrowBack: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f); lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f); lineTo(20f, 13f); close()
        }
    }.build()

private val CustomEditIcon: ImageVector
    get() = ImageVector.Builder(name = "Edit", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF6B7280))) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f); lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f); arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f); arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f); lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

@Composable
fun ReviewProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgGray)
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Kiểm tra lại thông tin",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TitleBlue
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Card 1: Profile Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).background(Color(0xFFE5E7EB), CircleShape))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Nguyễn Minh Khánh", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TitleBlue)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("24 tuổi • Nữ • Designer", fontSize = 13.sp, color = TextGray)
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("GIỚI THIỆU BẢN THÂN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextGray, letterSpacing = 0.5.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = CustomEditIcon, contentDescription = "Edit", modifier = Modifier.size(14.dp))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Mình sống khá yên tĩnh, gọn gàng, thích nấu ăn cuối tuần. Mong tìm được bạn roommate tử tế, văn minh để ở ghép ổn định lâu dài.",
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 20.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Card 2: Needs Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Ngân sách", fontSize = 13.sp, color = TextGray)
                    Text("2-5 triệu / tháng", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Khu vực tìm kiếm", fontSize = 13.sp, color = TextGray)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Quận 1, Quận 3", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        Text("TP. Hồ Chí Minh", fontSize = 11.sp, color = TextGray)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Card 3: Lifestyle Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Text("Thói quen & Lối sống", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.background(BgGray, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Không hút thuốc", fontSize = 12.sp, color = TextGray)
                    }
                    Box(modifier = Modifier.background(BgGray, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Thích thú cưng", fontSize = 12.sp, color = TextGray)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Box(modifier = Modifier.background(BgGray, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Rất cần riêng tư", fontSize = 12.sp, color = TextGray)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Bottom Action
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = CustomArrowBack, contentDescription = "Back", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quay lại", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = BluePrimary)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Hoàn tất hồ sơ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewProfileScreenPreview() {
    HomEverTheme {
        ReviewProfileScreen()
    }
}
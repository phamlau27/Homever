package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.homever.ui.theme.*

private val ProfEditIcon: ImageVector
    get() = ImageVector.Builder(name = "Edit", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f); lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f); arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f); arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f); lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

private val ProfSettingsIcon: ImageVector
    get() = ImageVector.Builder(name = "Settings", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(19.14f, 12.94f); curveTo(19.19f, 12.63f, 19.22f, 12.32f, 19.22f, 12f); curveTo(19.22f, 11.68f, 19.19f, 11.37f, 19.14f, 11.06f); lineTo(21.16f, 9.48f); curveTo(21.34f, 9.34f, 21.39f, 9.08f, 21.28f, 8.87f); lineTo(19.36f, 5.55f); curveTo(19.25f, 5.34f, 18.99f, 5.25f, 18.78f, 5.34f); lineTo(16.4f, 6.29f); curveTo(15.91f, 5.92f, 15.37f, 5.6f, 14.79f, 5.36f); lineTo(14.43f, 2.82f); curveTo(14.4f, 2.59f, 14.2f, 2.42f, 13.96f, 2.42f); lineTo(10.04f, 2.42f); curveTo(9.8f, 2.42f, 9.6f, 2.59f, 9.57f, 2.82f); lineTo(9.21f, 5.36f); curveTo(8.63f, 5.6f, 8.09f, 5.92f, 7.6f, 6.29f); lineTo(5.22f, 5.34f); curveTo(5.01f, 5.25f, 4.75f, 5.34f, 4.64f, 5.55f); lineTo(2.72f, 8.87f); curveTo(2.61f, 9.08f, 2.66f, 9.34f, 2.84f, 9.48f); lineTo(4.86f, 11.06f); curveTo(4.81f, 11.37f, 4.78f, 11.68f, 4.78f, 12f); curveTo(4.78f, 12.32f, 4.81f, 12.63f, 4.86f, 12.94f); lineTo(2.84f, 14.52f); curveTo(2.66f, 14.66f, 2.61f, 14.92f, 2.72f, 15.13f); lineTo(4.64f, 18.45f); curveTo(4.75f, 18.66f, 5.01f, 18.75f, 5.22f, 18.66f); lineTo(7.6f, 17.71f); curveTo(8.09f, 18.08f, 8.63f, 18.4f, 9.21f, 18.64f); lineTo(9.57f, 21.18f); curveTo(9.6f, 21.41f, 9.8f, 21.58f, 10.04f, 21.58f); lineTo(13.96f, 21.58f); curveTo(14.2f, 21.58f, 14.4f, 21.41f, 14.43f, 21.18f); lineTo(14.79f, 18.64f); curveTo(15.37f, 18.4f, 15.91f, 18.08f, 16.4f, 17.71f); lineTo(18.78f, 18.66f); curveTo(18.99f, 18.75f, 19.25f, 18.66f, 19.36f, 18.45f); lineTo(21.28f, 15.13f); curveTo(21.39f, 14.92f, 21.34f, 14.66f, 21.16f, 14.52f); lineTo(19.14f, 12.94f); close(); moveTo(12f, 15.5f); curveTo(10.07f, 15.5f, 8.5f, 13.93f, 8.5f, 12f); curveTo(8.5f, 10.07f, 10.07f, 8.5f, 12f, 8.5f); curveTo(13.93f, 8.5f, 15.5f, 10.07f, 15.5f, 12f); curveTo(15.5f, 13.93f, 13.93f, 15.5f, 12f, 15.5f); close()
        }
    }.build()

@Composable
fun PersonalProfileScreen(
    displayName: String = "Tôi",
    age: String = "",
    occupation: String = "",
    bio: String = "",
    onSettingsClick: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    val displayHeader = listOf(displayName.ifBlank { "Hồ sơ của tôi" }, if (age.isNotBlank()) "$age tuổi" else null)
        .filterNotNull().joinToString(", ")

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "profile",
                onProfileClick = {},
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onMessagesClick = onNavigateToMessages
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGray)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hồ sơ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, HomeverGrayBorder, CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable { onSettingsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = ProfSettingsIcon, contentDescription = "Settings", modifier = Modifier.size(20.dp), tint = HomeverTextGray)
                }
            }
            
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                // Avatar initial circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(BluePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayName.take(1).uppercase().ifBlank { "U" },
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = displayHeader,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomeverTextDark
                )
                
                if (occupation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = occupation,
                        fontSize = 14.sp,
                        color = TextGray
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // My Intro
                Text("Giới thiệu của tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfIntroCard(
                    title = "Giới thiệu bản thân",
                    subtitle = bio.ifBlank { "Chưa cập nhật phần giới thiệu bản thân." },
                    iconBg = Color(0xFFE0E7FF),
                    iconColor = BluePrimary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ProfIntroCard(
                    title = "Ở cùng mình sẽ như thế nào?",
                    subtitle = "Tôn trọng không gian riêng, giữ vệ sinh chung, văn minh và lịch sự.",
                    iconBg = Color(0xFFD1FAE5),
                    iconColor = Color(0xFF059669)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ProfIntroCard(
                    title = "Roommate mong muốn",
                    subtitle = "Tìm bạn trung thực, vui vẻ, không hút thuốc và sống hòa đồng.",
                    iconBg = Color(0xFFFFEDD5),
                    iconColor = Color(0xFFEA580C)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfIntroCard(title: String, subtitle: String, iconBg: Color, iconColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(32.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(16.dp).background(iconColor, CircleShape))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 13.sp, color = TextGray, lineHeight = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfileScreenPreview() {
    HomEverTheme {
        PersonalProfileScreen()
    }
}

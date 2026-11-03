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

private val ProfBlue = Color(0xFF2962FF)
private val ProfBgGray = Color(0xFFF9FAFB)
private val ProfTextGray = Color(0xFF6B7280)

private val ProfEditIcon: ImageVector
    get() = ImageVector.Builder(name = "Edit", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF6B7280))) {
            moveTo(3f, 17.25f); lineTo(3f, 21f); lineTo(6.75f, 21f); lineTo(17.81f, 9.94f); lineTo(14.06f, 6.19f); lineTo(3f, 17.25f); close()
            moveTo(20.71f, 7.04f); arcTo(0.996f, 0.996f, 0f, false, false, 20.71f, 5.63f); lineTo(18.37f, 3.29f); arcTo(0.996f, 0.996f, 0f, false, false, 16.96f, 3.29f); lineTo(15.13f, 5.12f); lineTo(18.88f, 8.87f); lineTo(20.71f, 7.04f); close()
        }
    }.build()

private val ProfSettingsIcon: ImageVector
    get() = ImageVector.Builder(name = "Settings", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF6B7280))) {
            moveTo(19.14f, 12.94f); curveTo(19.19f, 12.63f, 19.22f, 12.32f, 19.22f, 12f); curveTo(19.22f, 11.68f, 19.19f, 11.37f, 19.14f, 11.06f); lineTo(21.16f, 9.48f); curveTo(21.34f, 9.34f, 21.39f, 9.08f, 21.28f, 8.87f); lineTo(19.36f, 5.55f); curveTo(19.25f, 5.34f, 18.99f, 5.25f, 18.78f, 5.34f); lineTo(16.4f, 6.29f); curveTo(15.91f, 5.92f, 15.37f, 5.6f, 14.79f, 5.36f); lineTo(14.43f, 2.82f); curveTo(14.4f, 2.59f, 14.2f, 2.42f, 13.96f, 2.42f); lineTo(10.04f, 2.42f); curveTo(9.8f, 2.42f, 9.6f, 2.59f, 9.57f, 2.82f); lineTo(9.21f, 5.36f); curveTo(8.63f, 5.6f, 8.09f, 5.92f, 7.6f, 6.29f); lineTo(5.22f, 5.34f); curveTo(5.01f, 5.25f, 4.75f, 5.34f, 4.64f, 5.55f); lineTo(2.72f, 8.87f); curveTo(2.61f, 9.08f, 2.66f, 9.34f, 2.84f, 9.48f); lineTo(4.86f, 11.06f); curveTo(4.81f, 11.37f, 4.78f, 11.68f, 4.78f, 12f); curveTo(4.78f, 12.32f, 4.81f, 12.63f, 4.86f, 12.94f); lineTo(2.84f, 14.52f); curveTo(2.66f, 14.66f, 2.61f, 14.92f, 2.72f, 15.13f); lineTo(4.64f, 18.45f); curveTo(4.75f, 18.66f, 5.01f, 18.75f, 5.22f, 18.66f); lineTo(7.6f, 17.71f); curveTo(8.09f, 18.08f, 8.63f, 18.4f, 9.21f, 18.64f); lineTo(9.57f, 21.18f); curveTo(9.6f, 21.41f, 9.8f, 21.58f, 10.04f, 21.58f); lineTo(13.96f, 21.58f); curveTo(14.2f, 21.58f, 14.4f, 21.41f, 14.43f, 21.18f); lineTo(14.79f, 18.64f); curveTo(15.37f, 18.4f, 15.91f, 18.08f, 16.4f, 17.71f); lineTo(18.78f, 18.66f); curveTo(18.99f, 18.75f, 19.25f, 18.66f, 19.36f, 18.45f); lineTo(21.28f, 15.13f); curveTo(21.39f, 14.92f, 21.34f, 14.66f, 21.16f, 14.52f); lineTo(19.14f, 12.94f); close(); moveTo(12f, 15.5f); curveTo(10.07f, 15.5f, 8.5f, 13.93f, 8.5f, 12f); curveTo(8.5f, 10.07f, 10.07f, 8.5f, 12f, 8.5f); curveTo(13.93f, 8.5f, 15.5f, 10.07f, 15.5f, 12f); curveTo(15.5f, 13.93f, 13.93f, 15.5f, 12f, 15.5f); close()
        }
    }.build()

// Standard Nav Icons
private val NavFlame: ImageVector get() = ImageVector.Builder(name = "Flame", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(17.55f, 11.2f); curveTo(17.32f, 10.9f, 17.05f, 10.64f, 16.79f, 10.38f); curveTo(16.32f, 9.9f, 15.82f, 9.45f, 15.42f, 8.91f); curveTo(14.61f, 7.82f, 14.28f, 6.42f, 14.49f, 5.09f); curveTo(14.53f, 4.84f, 14.39f, 4.59f, 14.16f, 4.49f); curveTo(13.93f, 4.39f, 13.66f, 4.46f, 13.51f, 4.65f); curveTo(11.95f, 6.6f, 11.03f, 9.07f, 11.19f, 11.59f); curveTo(11.23f, 12.11f, 10.96f, 12.63f, 10.53f, 12.87f); curveTo(10.1f, 13.11f, 9.58f, 13.06f, 9.2f, 12.75f); curveTo(8.75f, 12.38f, 8.42f, 11.89f, 8.24f, 11.34f); curveTo(8.13f, 11.04f, 7.74f, 10.93f, 7.48f, 11.13f); curveTo(5.99f, 12.24f, 5.06f, 14.04f, 5.0f, 15.96f); curveTo(4.91f, 18.7f, 6.48f, 21.26f, 8.94f, 22.45f); curveTo(11.39f, 23.64f, 14.37f, 23.27f, 16.48f, 21.49f); curveTo(18.59f, 19.7f, 19.53f, 16.81f, 18.88f, 14.15f); curveTo(18.66f, 13.16f, 18.19f, 12.18f, 17.55f, 11.2f); close() } }.build()
private val NavHome: ImageVector get() = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close() } }.build()
private val NavHeart: ImageVector get() = ImageVector.Builder(name = "Heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 21.35f); lineTo(10.55f, 20.03f); curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f); curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f); curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f); curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f); curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f); curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f); lineTo(12f, 21.35f); close() } }.build()
private val NavChat: ImageVector get() = ImageVector.Builder(name = "Chat", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(20f, 2f); lineTo(4f, 2f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); lineTo(2f, 22f); lineTo(6f, 18f); lineTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); lineTo(22f, 4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close() } }.build()
private val NavPerson: ImageVector get() = ImageVector.Builder(name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 12f); curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f); curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f); curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f); curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f); close(); moveTo(12f, 14f); curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 18f); curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f); close() } }.build()

@Composable
fun PersonalProfileScreen() {
    Scaffold(
        bottomBar = { ProfBottomNavigation("profile") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfBgGray)
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
                    color = ProfBlue,
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.size(40.dp).border(1.dp, Color(0xFFE5E7EB), CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = ProfEditIcon, contentDescription = "Edit", modifier = Modifier.size(20.dp), tint = ProfTextGray)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(40.dp).border(1.dp, Color(0xFFE5E7EB), CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = ProfSettingsIcon, contentDescription = "Settings", modifier = Modifier.size(20.dp), tint = ProfTextGray)
                }
            }
            
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                // Avatar
                Box(modifier = Modifier.size(80.dp).background(Color(0xFFE5E7EB), CircleShape))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Nguyễn Minh Trung, 22", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(modifier = Modifier.border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)).background(Color.White, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text("Xem trước", fontSize = 12.sp, color = ProfTextGray, fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // My Photos Card
                Box(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(20.dp)).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(20.dp)).padding(16.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Ảnh của tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                            Text("Chỉnh sửa", fontSize = 13.sp, color = ProfBlue, fontWeight = FontWeight.Medium)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Main large photo
                            Box(modifier = Modifier.weight(1f).aspectRatio(0.7f).background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp)))
                            
                            // Right column of small photos
                            Column(modifier = Modifier.weight(0.8f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp)))
                                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp)))
                                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).background(Color(0xFFE5E7EB), RoundedCornerShape(12.dp)))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f).aspectRatio(2f).background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Box(modifier = Modifier.size(24.dp).background(Color(0xFFDDF4FF), CircleShape), contentAlignment = Alignment.Center) { Text("+", color = ProfBlue) }
                            }
                            Box(modifier = Modifier.weight(1f).aspectRatio(2f).background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Box(modifier = Modifier.size(24.dp).background(Color(0xFFDDF4FF), CircleShape), contentAlignment = Alignment.Center) { Text("+", color = ProfBlue) }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // My Intro
                Text("Giới thiệu của tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Spacer(modifier = Modifier.height(16.dp))
                
                ProfIntroCard(
                    title = "Giới thiệu bản thân",
                    subtitle = "Thêm vài dòng để mọi người hiểu hơn về bạn.",
                    iconBg = Color(0xFFE0E7FF),
                    iconColor = ProfBlue
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ProfIntroCard(
                    title = "Ở cùng mình sẽ như thế nào?",
                    subtitle = "Chia sẻ điều thú vị khi trở thành bạn cùng nhà của bạn.",
                    iconBg = Color(0xFFD1FAE5),
                    iconColor = Color(0xFF059669)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ProfIntroCard(
                    title = "Mình đang tìm một roommate như thế nào?",
                    subtitle = "Kể về người bạn cùng nhà mà bạn mong muốn.",
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
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(32.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(16.dp).background(iconColor, CircleShape)) // Mock icon
                }
                Box(modifier = Modifier.size(32.dp).background(Color(0xFFF3F4F6), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = ProfEditIcon, contentDescription = "Edit", modifier = Modifier.size(16.dp), tint = ProfTextGray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = ProfTextGray)
        }
    }
}

@Composable
fun ProfBottomNavigation(selectedItem: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(NavFlame, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = ProfBlue, selectedTextColor = ProfBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHome, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = ProfBlue, selectedTextColor = ProfBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHeart, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = ProfBlue, selectedTextColor = ProfBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavChat, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = ProfBlue, selectedTextColor = ProfBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavPerson, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = ProfBlue, selectedTextColor = ProfBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfileScreenPreview() {
    HomEverTheme {
        PersonalProfileScreen()
    }
}
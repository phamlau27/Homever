package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val RoomBlue = Color(0xFF2962FF)
private val RoomBgGray = Color(0xFFF9FAFB)
private val RoomTextGray = Color(0xFF6B7280)

private val RoomSearchIcon: ImageVector
    get() = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(RoomBlue), strokeLineWidth = 2f) {
            moveTo(21f, 21f); lineTo(15f, 15f); moveTo(17f, 10f); arcTo(7f, 7f, 0f, true, true, 3f, 10f); arcTo(7f, 7f, 0f, true, true, 17f, 10f); close()
        }
    }.build()

private val RoomFilterIcon: ImageVector
    get() = ImageVector.Builder(name = "Filter", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(RoomBlue), strokeLineWidth = 2f) {
            moveTo(4f, 6f); lineTo(20f, 6f); moveTo(4f, 12f); lineTo(20f, 12f); moveTo(4f, 18f); lineTo(20f, 18f);
            moveTo(8f, 4f); lineTo(8f, 8f); moveTo(16f, 10f); lineTo(16f, 14f); moveTo(10f, 16f); lineTo(10f, 20f)
        }
    }.build()

private val RoomHeartOutlinedIcon: ImageVector
    get() = ImageVector.Builder(name = "HeartOutlined", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(Color(0xFFF97316)), strokeLineWidth = 2f) {
             moveTo(20.84f, 4.61f); arcTo(5.5f, 5.5f, 0f, false, false, 12f, 5.61f); arcTo(5.5f, 5.5f, 0f, false, false, 3.16f, 4.61f); curveTo(1.46f, 6.31f, 1.37f, 9.17f, 2.95f, 11.02f); lineTo(12f, 20.07f); lineTo(21.05f, 11.02f); curveTo(22.63f, 9.17f, 22.54f, 6.31f, 20.84f, 4.61f); close()
        }
    }.build()

// Standard Nav Icons
private val NavFlame: ImageVector get() = ImageVector.Builder(name = "Flame", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(17.55f, 11.2f); curveTo(17.32f, 10.9f, 17.05f, 10.64f, 16.79f, 10.38f); curveTo(16.32f, 9.9f, 15.82f, 9.45f, 15.42f, 8.91f); curveTo(14.61f, 7.82f, 14.28f, 6.42f, 14.49f, 5.09f); curveTo(14.53f, 4.84f, 14.39f, 4.59f, 14.16f, 4.49f); curveTo(13.93f, 4.39f, 13.66f, 4.46f, 13.51f, 4.65f); curveTo(11.95f, 6.6f, 11.03f, 9.07f, 11.19f, 11.59f); curveTo(11.23f, 12.11f, 10.96f, 12.63f, 10.53f, 12.87f); curveTo(10.1f, 13.11f, 9.58f, 13.06f, 9.2f, 12.75f); curveTo(8.75f, 12.38f, 8.42f, 11.89f, 8.24f, 11.34f); curveTo(8.13f, 11.04f, 7.74f, 10.93f, 7.48f, 11.13f); curveTo(5.99f, 12.24f, 5.06f, 14.04f, 5.0f, 15.96f); curveTo(4.91f, 18.7f, 6.48f, 21.26f, 8.94f, 22.45f); curveTo(11.39f, 23.64f, 14.37f, 23.27f, 16.48f, 21.49f); curveTo(18.59f, 19.7f, 19.53f, 16.81f, 18.88f, 14.15f); curveTo(18.66f, 13.16f, 18.19f, 12.18f, 17.55f, 11.2f); close() } }.build()
private val NavHome: ImageVector get() = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close() } }.build()
private val NavHeart: ImageVector get() = ImageVector.Builder(name = "Heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 21.35f); lineTo(10.55f, 20.03f); curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f); curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f); curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f); curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f); curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f); curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f); lineTo(12f, 21.35f); close() } }.build()
private val NavChat: ImageVector get() = ImageVector.Builder(name = "Chat", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(20f, 2f); lineTo(4f, 2f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); lineTo(2f, 22f); lineTo(6f, 18f); lineTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); lineTo(22f, 4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close() } }.build()
private val NavPerson: ImageVector get() = ImageVector.Builder(name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 12f); curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f); curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f); curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f); curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f); close(); moveTo(12f, 14f); curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 18f); curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f); close() } }.build()

@Composable
fun RoomBrowsingScreen() {
    Scaffold(
        bottomBar = { RoomBottomNavigation("room") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RoomBgGray)
                .padding(innerPadding)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phòng",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoomBlue,
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.size(40.dp).border(1.dp, Color(0xFFE5E7EB), CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = RoomSearchIcon, contentDescription = "Search", modifier = Modifier.size(20.dp), tint = RoomBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(40.dp).border(1.dp, Color(0xFFE5E7EB), CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = RoomFilterIcon, contentDescription = "Filter", modifier = Modifier.size(20.dp), tint = RoomBlue)
                }
            }
            
            // Subtitle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gợi ý cho bạn", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                Text("12 phòng", fontSize = 13.sp, color = RoomBlue, fontWeight = FontWeight.Medium)
            }
            
            // List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    RoomCard(
                        title = "Phòng studio view sông, Thủ Đức",
                        price = "3.500.000đ/tháng",
                        details = "Thủ Đức • 25m² • Tối đa 2 người",
                        tags = listOf("Máy lạnh", "Không chung chủ", "Cho ở 2 người")
                    )
                }
                item {
                    RoomCard(
                        title = "Phòng riêng nội thất cơ bản, Tân Bình",
                        price = "2.800.000đ/tháng",
                        details = "Tân Bình • 18m² • Tối đa 2 người",
                        tags = listOf("Nội thất cơ bản", "Tủ lạnh", "Gần trường học")
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCard(title: String, price: String, details: String, tags: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
    ) {
        Column {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFE5E7EB), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                // Badges
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color(0xFFD1FAE5), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text("Còn trống", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
                }
                
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = RoomHeartOutlinedIcon, contentDescription = "Favorite", modifier = Modifier.size(20.dp), tint = Color.Unspecified)
                }
            }
            
            // Info
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Spacer(modifier = Modifier.height(4.dp))
                Text(price, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = RoomBlue)
                Spacer(modifier = Modifier.height(6.dp))
                Text(details, fontSize = 12.sp, color = RoomTextGray)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(tag, fontSize = 11.sp, color = RoomTextGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomBottomNavigation(selectedItem: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(NavFlame, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = RoomBlue, selectedTextColor = RoomBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHome, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = RoomBlue, selectedTextColor = RoomBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHeart, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = RoomBlue, selectedTextColor = RoomBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavChat, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = RoomBlue, selectedTextColor = RoomBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavPerson, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = RoomBlue, selectedTextColor = RoomBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoomBrowsingScreenPreview() {
    HomEverTheme {
        RoomBrowsingScreen()
    }
}
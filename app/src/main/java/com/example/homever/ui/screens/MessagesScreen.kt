package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

private val MsgBlue = Color(0xFF2962FF)
private val MsgBgGray = Color(0xFFF9FAFB)
private val MsgTextGray = Color(0xFF6B7280)

private val MsgSearchIcon: ImageVector
    get() = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(Color.Gray), strokeLineWidth = 2f) {
            moveTo(21f, 21f); lineTo(15f, 15f); moveTo(17f, 10f); arcTo(7f, 7f, 0f, true, true, 3f, 10f); arcTo(7f, 7f, 0f, true, true, 17f, 10f); close()
        }
    }.build()

// Standard Nav Icons
private val NavFlame: ImageVector get() = ImageVector.Builder(name = "Flame", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(17.55f, 11.2f); curveTo(17.32f, 10.9f, 17.05f, 10.64f, 16.79f, 10.38f); curveTo(16.32f, 9.9f, 15.82f, 9.45f, 15.42f, 8.91f); curveTo(14.61f, 7.82f, 14.28f, 6.42f, 14.49f, 5.09f); curveTo(14.53f, 4.84f, 14.39f, 4.59f, 14.16f, 4.49f); curveTo(13.93f, 4.39f, 13.66f, 4.46f, 13.51f, 4.65f); curveTo(11.95f, 6.6f, 11.03f, 9.07f, 11.19f, 11.59f); curveTo(11.23f, 12.11f, 10.96f, 12.63f, 10.53f, 12.87f); curveTo(10.1f, 13.11f, 9.58f, 13.06f, 9.2f, 12.75f); curveTo(8.75f, 12.38f, 8.42f, 11.89f, 8.24f, 11.34f); curveTo(8.13f, 11.04f, 7.74f, 10.93f, 7.48f, 11.13f); curveTo(5.99f, 12.24f, 5.06f, 14.04f, 5.0f, 15.96f); curveTo(4.91f, 18.7f, 6.48f, 21.26f, 8.94f, 22.45f); curveTo(11.39f, 23.64f, 14.37f, 23.27f, 16.48f, 21.49f); curveTo(18.59f, 19.7f, 19.53f, 16.81f, 18.88f, 14.15f); curveTo(18.66f, 13.16f, 18.19f, 12.18f, 17.55f, 11.2f); close() } }.build()
private val NavHome: ImageVector get() = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close() } }.build()
private val NavHeart: ImageVector get() = ImageVector.Builder(name = "Heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 21.35f); lineTo(10.55f, 20.03f); curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f); curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f); curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f); curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f); curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f); curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f); lineTo(12f, 21.35f); close() } }.build()
private val NavChat: ImageVector get() = ImageVector.Builder(name = "Chat", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(20f, 2f); lineTo(4f, 2f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); lineTo(2f, 22f); lineTo(6f, 18f); lineTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); lineTo(22f, 4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close() } }.build()
private val NavPerson: ImageVector get() = ImageVector.Builder(name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply { path(fill = SolidColor(Color.Black)) { moveTo(12f, 12f); curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f); curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f); curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f); curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f); close(); moveTo(12f, 14f); curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 18f); curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f); close() } }.build()

@Composable
fun MessagesScreen() {
    Scaffold(
        bottomBar = { MsgBottomNavigation("messages") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            Text(
                text = "Tin nhắn",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MsgBlue,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
            
            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(MsgBgGray, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = MsgSearchIcon, contentDescription = "Search", modifier = Modifier.size(20.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Tìm cuộc trò chuyện...", color = Color(0xFFA0AAB0), fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Segmented Tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFDDF4FF), RoundedCornerShape(24.dp))
                        .border(1.dp, MsgBlue, RoundedCornerShape(24.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bạn cùng phòng", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MsgBlue)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(24.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Phòng trọ", fontSize = 14.sp, color = MsgTextGray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // New Connections
            Text("KẾT NỐI MỚI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MsgTextGray, modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val newConnections = listOf("Minh Anh", "Hải Nam", "Khánh Linh", "Duy Mạnh")
                items(newConnections.size) { index ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .border(2.dp, MsgBlue, CircleShape)
                                .padding(2.dp)
                                .background(Color(0xFFE5E7EB), CircleShape)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(newConnections[index], fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1F2937))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Recent Chats
            Text("TRÒ CHUYỆN GẦN ĐÂY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MsgTextGray, modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { ChatListItem("Trần Thu Hà", "Mình cũng đang tìm phòng ở Thủ Đ...", "2 phút trước", unread = 2) }
                item { ChatListItem("Lê Minh Tuấn", "Ok bạn nhé, vậy 7h tối nay tụi mình g...", "10:30") }
                item { ChatListItem("Hoàng Yến", "Mình cũng thích phòng ở gần trung...", "Hôm qua") }
                item { ChatListItem("Khánh Linh", "Bạn có muốn đi xem phòng chung v...", "24 Th1") }
            }
        }
    }
}

@Composable
fun ChatListItem(name: String, message: String, time: String, unread: Int = 0) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(modifier = Modifier.size(50.dp)) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB), CircleShape))
                // Online dot
                Box(modifier = Modifier.size(12.dp).align(Alignment.BottomEnd).background(Color(0xFF10B981), CircleShape).border(2.dp, Color.White, CircleShape))
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    Text(time, fontSize = 11.sp, color = MsgTextGray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(message, fontSize = 13.sp, color = if (unread > 0) Color.Black else MsgTextGray, fontWeight = if (unread > 0) FontWeight.Medium else FontWeight.Normal, maxLines = 1, modifier = Modifier.weight(1f))
                    if (unread > 0) {
                        Box(modifier = Modifier.padding(start = 8.dp).size(20.dp).background(MsgBlue, CircleShape), contentAlignment = Alignment.Center) {
                            Text(unread.toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.background(Color(0xFFD1FAE5), RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("Đã cùng quan tâm", fontSize = 10.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun MsgBottomNavigation(selectedItem: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(NavFlame, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MsgBlue, selectedTextColor = MsgBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHome, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MsgBlue, selectedTextColor = MsgBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavHeart, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MsgBlue, selectedTextColor = MsgBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavChat, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MsgBlue, selectedTextColor = MsgBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(NavPerson, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MsgBlue, selectedTextColor = MsgBlue, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    HomEverTheme {
        MessagesScreen()
    }
}
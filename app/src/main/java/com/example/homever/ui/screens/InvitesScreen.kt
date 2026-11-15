package com.example.homever.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

val InvitesBluePrimary = Color(0xFF2962FF)

private val CustomLocationIconGray: ImageVector
    get() = ImageVector.Builder(name = "LocationGray", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Gray)) {
            moveTo(12f, 2f); curveTo(8.13f, 2f, 5f, 5.13f, 5f, 9f); curveTo(5f, 14.25f, 12f, 22f, 12f, 22f); curveTo(12f, 22f, 19f, 14.25f, 19f, 9f); curveTo(19f, 5.13f, 15.87f, 2f, 12f, 2f); close(); moveTo(12f, 11.5f); curveTo(10.62f, 11.5f, 9.5f, 10.38f, 9.5f, 9f); curveTo(9.5f, 7.62f, 10.62f, 6.5f, 12f, 6.5f); curveTo(13.38f, 6.5f, 14.5f, 7.62f, 14.5f, 9f); curveTo(14.5f, 10.38f, 13.38f, 11.5f, 12f, 11.5f); close()
        }
    }.build()

private val WalletIconGray: ImageVector
    get() = ImageVector.Builder(name = "WalletGray", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Gray)) {
            moveTo(21f, 7.28f); lineTo(21f, 5f); curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f); lineTo(5f, 3f); curveTo(3.89f, 3f, 3f, 3.9f, 3f, 5f); lineTo(3f, 19f); curveTo(3f, 20.1f, 3.89f, 21f, 5f, 21f); lineTo(19f, 21f); curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f); lineTo(21f, 16.72f); curveTo(21.59f, 16.2f, 22f, 15.15f, 22f, 14f); lineTo(22f, 10f); curveTo(22f, 8.85f, 21.59f, 7.8f, 21f, 7.28f); close(); moveTo(20f, 14f); curveTo(20f, 14.55f, 19.55f, 15f, 19f, 15f); curveTo(18.45f, 15f, 18f, 14.55f, 18f, 14f); curveTo(18f, 13.45f, 18.45f, 13f, 19f, 13f); curveTo(19.55f, 13f, 20f, 13.45f, 20f, 14f); close()
        }
    }.build()
    
private val HeartIcon: ImageVector
    get() = ImageVector.Builder(name = "Heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 21.35f); lineTo(10.55f, 20.03f); curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f); curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f); curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f); curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f); curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f); curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f); lineTo(12f, 21.35f); close()
        }
    }.build()

private val ChatIcon: ImageVector
    get() = ImageVector.Builder(name = "Chat", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 2f); lineTo(4f, 2f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); lineTo(2f, 22f); lineTo(6f, 18f); lineTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); lineTo(22f, 4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close()
        }
    }.build()

private val FlameIcon: ImageVector
    get() = ImageVector.Builder(name = "Flame", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(17.55f, 11.2f); curveTo(17.32f, 10.9f, 17.05f, 10.64f, 16.79f, 10.38f); curveTo(16.32f, 9.9f, 15.82f, 9.45f, 15.42f, 8.91f); curveTo(14.61f, 7.82f, 14.28f, 6.42f, 14.49f, 5.09f); curveTo(14.53f, 4.84f, 14.39f, 4.59f, 14.16f, 4.49f); curveTo(13.93f, 4.39f, 13.66f, 4.46f, 13.51f, 4.65f); curveTo(11.95f, 6.6f, 11.03f, 9.07f, 11.19f, 11.59f); curveTo(11.23f, 12.11f, 10.96f, 12.63f, 10.53f, 12.87f); curveTo(10.1f, 13.11f, 9.58f, 13.06f, 9.2f, 12.75f); curveTo(8.75f, 12.38f, 8.42f, 11.89f, 8.24f, 11.34f); curveTo(8.13f, 11.04f, 7.74f, 10.93f, 7.48f, 11.13f); curveTo(5.99f, 12.24f, 5.06f, 14.04f, 5.0f, 15.96f); curveTo(4.91f, 18.7f, 6.48f, 21.26f, 8.94f, 22.45f); curveTo(11.39f, 23.64f, 14.37f, 23.27f, 16.48f, 21.49f); curveTo(18.59f, 19.7f, 19.53f, 16.81f, 18.88f, 14.15f); curveTo(18.66f, 13.16f, 18.19f, 12.18f, 17.55f, 11.2f); close()
        }
    }.build()

private val InvitesHomeIcon: ImageVector
    get() = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

private val InvitesPersonIcon: ImageVector
    get() = ImageVector.Builder(name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 12f); curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f); curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f); curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f); curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f); close(); moveTo(12f, 14f); curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 18f); curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f); close()
        }
    }.build()

@Composable
fun InvitesScreen(modifier: Modifier = Modifier, initialTab: Int = 0) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    
    Scaffold(
        bottomBar = { InvitesBottomNavigation("invites") }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(innerPadding)
        ) {
            Text(
                "Lượt quan tâm", 
                fontSize = 24.sp, 
                fontWeight = FontWeight.Bold, 
                color = InvitesBluePrimary, 
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            )
            
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = InvitesBluePrimary,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = InvitesBluePrimary
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { 
                        Text("Đã nhận", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal, color = if (selectedTab == 0) InvitesBluePrimary else Color.Gray)
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { 
                        Text("Đã gửi", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal, color = if (selectedTab == 1) InvitesBluePrimary else Color.Gray)
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val dummyList = listOf(
                UserInvite("Lê Minh Tuấn, 25", "Kỹ sư phần mềm", "Quận 3", "4 - 5tr"),
                UserInvite("Hoàng Yến, 25", "Sinh viên ĐH Ngoại Thương", "Quận 1", "3.5 - 4.5tr")
            )
            
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dummyList) { item ->
                    InviteCard(item = item, isReceived = selectedTab == 0)
                }
            }
        }
    }
}

data class UserInvite(val nameAge: String, val job: String, val location: String, val budget: String)

@Composable
fun InviteCard(item: UserInvite, isReceived: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).background(Color(0xFFE5E7EB), CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(item.nameAge, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    Text(item.job, fontSize = 12.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Icon(imageVector = CustomLocationIconGray, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.location, fontSize = 12.sp, color = Color(0xFF4B5563))
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Icon(imageVector = WalletIconGray, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.budget, fontSize = 12.sp, color = Color(0xFF4B5563))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallTag("Cú đêm")
                SmallTag("Gọn gàng")
                SmallTag("Không hút thuốc")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isReceived) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Text("Bỏ qua", color = Color(0xFF4B5563), fontSize = 14.sp)
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InvitesBluePrimary)
                    ) {
                        Text("Quan tâm", color = Color.White, fontSize = 14.sp)
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    val statusText = if (item.nameAge.contains("Tuấn")) "Đang chờ phản hồi" else "Đã cùng quan tâm"
                    val statusColor = if (item.nameAge.contains("Tuấn")) Color(0xFFF3F4F6) else Color(0xFFD1FAE5)
                    val statusTextColor = if (item.nameAge.contains("Tuấn")) Color(0xFF6B7280) else Color(0xFF059669)
                    
                    Box(modifier = Modifier.background(statusColor, RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text(statusText, fontSize = 12.sp, color = statusTextColor, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun SmallTag(text: String) {
    Box(modifier = Modifier.border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(text, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun InvitesBottomNavigation(selectedItem: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(FlameIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = InvitesBluePrimary, selectedTextColor = InvitesBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(InvitesHomeIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = InvitesBluePrimary, selectedTextColor = InvitesBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(HeartIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = InvitesBluePrimary, selectedTextColor = InvitesBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(ChatIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = InvitesBluePrimary, selectedTextColor = InvitesBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(InvitesPersonIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = InvitesBluePrimary, selectedTextColor = InvitesBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InvitesReceivedScreenPreview() {
    HomEverTheme {
        InvitesScreen(initialTab = 0)
    }
}

@Preview(showBackground = true)
@Composable
fun InvitesSentScreenPreview() {
    HomEverTheme {
        InvitesScreen(initialTab = 1)
    }
}
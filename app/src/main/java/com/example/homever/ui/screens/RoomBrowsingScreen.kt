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
import com.example.homever.ui.theme.*

private val RoomSearchIcon: ImageVector
    get() = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(BluePrimary), strokeLineWidth = 2f) {
            moveTo(21f, 21f); lineTo(15f, 15f); moveTo(17f, 10f); arcTo(7f, 7f, 0f, true, true, 3f, 10f); arcTo(7f, 7f, 0f, true, true, 17f, 10f); close()
        }
    }.build()

private val RoomFilterIcon: ImageVector
    get() = ImageVector.Builder(name = "Filter", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(BluePrimary), strokeLineWidth = 2f) {
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

@Composable
fun RoomBrowsingScreen(
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToInvites: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "room",
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = {},
                onMessagesClick = onNavigateToMessages,
                onProfileClick = onNavigateToProfile,
                onInvitesClick = onNavigateToInvites
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGray)
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
                    color = BluePrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.size(40.dp).border(1.dp, HomeverGrayBorder, CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = RoomSearchIcon, contentDescription = "Search", modifier = Modifier.size(20.dp), tint = BluePrimary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(40.dp).border(1.dp, HomeverGrayBorder, CircleShape).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = RoomFilterIcon, contentDescription = "Filter", modifier = Modifier.size(20.dp), tint = BluePrimary)
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
                Text("Gợi ý cho bạn", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = HomeverTextDark)
                Text("12 phòng", fontSize = 13.sp, color = BluePrimary, fontWeight = FontWeight.Medium)
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
                item {
                    RoomCard(
                        title = "Căn hộ dịch vụ mini, Quận 3",
                        price = "5.000.000đ/tháng",
                        details = "Quận 3 • 30m² • Đầy đủ tiện nghi",
                        tags = listOf("Full nội thất", "Bảo vệ 24/7", "Ban công")
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
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
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
                    Text("Còn trống", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = HomeverGreen)
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
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(price, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(details, fontSize = 12.sp, color = TextGray)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, HomeverGrayBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(tag, fontSize = 11.sp, color = TextGray)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomBrowsingScreenPreview() {
    HomEverTheme {
        RoomBrowsingScreen()
    }
}

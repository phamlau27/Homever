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
import com.example.homever.ui.theme.*

private val MsgSearchIcon: ImageVector
    get() = ImageVector.Builder(name = "Search", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(stroke = SolidColor(Color.Gray), strokeLineWidth = 2f) {
            moveTo(21f, 21f); lineTo(15f, 15f); moveTo(17f, 10f); arcTo(7f, 7f, 0f, true, true, 3f, 10f); arcTo(7f, 7f, 0f, true, true, 17f, 10f); close()
        }
    }.build()

@Composable
fun MessagesScreen(
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "messages",
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onMessagesClick = {},
                onProfileClick = onNavigateToProfile
            )
        }
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
                color = BluePrimary,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
            
            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(BgGray, RoundedCornerShape(24.dp))
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
                        .border(1.dp, BluePrimary, RoundedCornerShape(24.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bạn cùng phòng", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, HomeverGrayBorder, RoundedCornerShape(24.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Phòng trọ", fontSize = 14.sp, color = TextGray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // New Connections
            Text("KẾT NỐI MỚI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.padding(horizontal = 24.dp))
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
                                .border(2.dp, BluePrimary, CircleShape)
                                .padding(2.dp)
                                .background(Color(0xFFE5E7EB), CircleShape)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(newConnections[index], fontSize = 12.sp, fontWeight = FontWeight.Medium, color = HomeverTextDark)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Recent Chats
            Text("TRÒ CHUYỆN GẦN ĐÂY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray, modifier = Modifier.padding(horizontal = 24.dp))
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
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(modifier = Modifier.size(50.dp)) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB), CircleShape))
                // Online dot
                Box(modifier = Modifier.size(12.dp).align(Alignment.BottomEnd).background(HomeverGreen, CircleShape).border(2.dp, Color.White, CircleShape))
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomeverTextDark)
                    Text(time, fontSize = 11.sp, color = TextGray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(message, fontSize = 13.sp, color = if (unread > 0) Color.Black else TextGray, fontWeight = if (unread > 0) FontWeight.Medium else FontWeight.Normal, maxLines = 1, modifier = Modifier.weight(1f))
                    if (unread > 0) {
                        Box(modifier = Modifier.padding(start = 8.dp).size(20.dp).background(BluePrimary, CircleShape), contentAlignment = Alignment.Center) {
                            Text(unread.toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.background(Color(0xFFD1FAE5), RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("Đã cùng quan tâm", fontSize = 10.sp, color = HomeverGreen, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    HomEverTheme {
        MessagesScreen()
    }
}

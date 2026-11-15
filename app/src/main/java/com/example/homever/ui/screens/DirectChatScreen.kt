package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

private val BlueBubbleColor = Color(0xFF4361EE)
private val PhoneCallIcon: ImageVector
    get() = ImageVector.Builder(name = "PhoneCall", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF1E293B))) {
            moveTo(6.62f, 10.79f); curveTo(8.06f, 13.62f, 10.38f, 15.94f, 13.21f, 17.38f); lineTo(15.41f, 15.18f); curveTo(15.69f, 14.9f, 16.08f, 14.82f, 16.43f, 14.93f); curveTo(17.55f, 15.3f, 18.75f, 15.5f, 20f, 15.5f); curveTo(20.55f, 15.5f, 21f, 15.95f, 21f, 16.5f); lineTo(21f, 20f); curveTo(21f, 20.55f, 20.55f, 21f, 20f, 21f); curveTo(10.61f, 21f, 3f, 13.39f, 3f, 4f); curveTo(3f, 3.45f, 3.45f, 3f, 4f, 3f); lineTo(7.5f, 3f); curveTo(8.05f, 3f, 8.5f, 3.45f, 8.5f, 4f); curveTo(8.5f, 5.25f, 8.7f, 6.45f, 9.07f, 7.57f); curveTo(9.18f, 7.92f, 9.1f, 8.31f, 8.82f, 8.59f); lineTo(6.62f, 10.79f); close()
        }
    }.build()

private val HouseIcon: ImageVector
    get() = ImageVector.Builder(name = "House", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF1E293B))) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

private val BackNavIcon: ImageVector
    get() = ImageVector.Builder(name = "BackNav", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF1E293B))) {
            moveTo(15.41f, 7.41f); lineTo(14f, 6f); lineTo(8f, 12f); lineTo(14f, 18f); lineTo(15.41f, 16.59f); lineTo(10.83f, 12f); close()
        }
    }.build()

private val SendButtonIcon: ImageVector
    get() = ImageVector.Builder(name = "SendBtn", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(2.01f, 21f); lineTo(23f, 12f); lineTo(2.01f, 3f); lineTo(2f, 10f); lineTo(17f, 12f); lineTo(2f, 14f); close()
        }
    }.build()

sealed class DirectChatMessageItem {
    data class TextMsg(val text: String, val time: String, val isMe: Boolean) : DirectChatMessageItem()
    data class RoomCardMsg(val title: String, val address: String, val time: String) : DirectChatMessageItem()
}

@Composable
fun DirectChatScreen(
    modifier: Modifier = Modifier,
    userName: String = "Trần Thu Hà",
    onBack: () -> Unit = {},
    onViewProfile: () -> Unit = {}
) {
    var messageInput by remember { mutableStateOf("") }
    val chatItems = remember {
        mutableStateListOf<DirectChatMessageItem>(
            DirectChatMessageItem.TextMsg(
                text = "Chào bạn nha! Mình thấy bạn quan tâm đến phòng studio Quận 3 của mình trên Homever đúng không?",
                time = "10:25",
                isMe = false
            ),
            DirectChatMessageItem.TextMsg(
                text = "Đúng rồi Hà ơi, phòng đẹp quá. Không biết chiều nay tầm 5h mình ghé qua xem phòng được không bạn nhỉ?",
                time = "10:26",
                isMe = true
            ),
            DirectChatMessageItem.RoomCardMsg(
                title = "Phòng Studio Quận 3",
                address = "120/4 Nguyễn Đình Chiểu,\nP.6, Q.3",
                time = "10:28"
            )
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = BackNavIcon, contentDescription = "Back", modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Avatar with online dot
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clickable { onViewProfile() }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFCBD5E1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Avatar", fontSize = 9.sp, color = Color.Gray)
                        }
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.BottomEnd)
                                .background(Color(0xFF22C55E), CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onViewProfile() }
                    ) {
                        Text(userName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text("Đang trực tuyến", fontSize = 11.sp, color = Color(0xFF22C55E))
                    }

                    // Action buttons (Call + House icon)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFF8FAFC), CircleShape)
                                .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = PhoneCallIcon, contentDescription = "Call", modifier = Modifier.size(16.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFF8FAFC), CircleShape)
                                .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                                .clickable { onViewProfile() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = HouseIcon, contentDescription = "Room/Profile", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Plus (+) and Picture icon
                    Text("+", fontSize = 24.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Normal, modifier = Modifier.padding(horizontal = 4.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(20.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(4.dp)))

                    Spacer(modifier = Modifier.width(12.dp))

                    // Text Input Pill
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(24.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = messageInput,
                            onValueChange = { messageInput = it },
                            modifier = Modifier.weight(1f),
                            decorationBox = { innerTextField ->
                                if (messageInput.isEmpty()) {
                                    Text("Nhập tin nhắn...", color = Color(0xFF94A3B8), fontSize = 14.sp)
                                }
                                innerTextField()
                            }
                        )
                        Text("😊", fontSize = 16.sp, modifier = Modifier.padding(start = 6.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Round Send button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF3B6FE7), CircleShape)
                            .clickable(enabled = messageInput.isNotBlank()) {
                                chatItems.add(
                                    DirectChatMessageItem.TextMsg(
                                        text = messageInput,
                                        time = "10:30",
                                        isMe = true
                                    )
                                )
                                messageInput = ""
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = SendButtonIcon, contentDescription = "Send", modifier = Modifier.size(18.dp), tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // "HÔM NAY • 10:24" timestamp
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("HÔM NAY • 10:24", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                }
            }

            items(chatItems) { item ->
                when (item) {
                    is DirectChatMessageItem.TextMsg -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (item.isMe) Arrangement.End else Arrangement.Start,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (!item.isMe) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFCBD5E1))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(
                                modifier = Modifier.widthIn(max = 280.dp),
                                horizontalAlignment = if (item.isMe) Alignment.End else Alignment.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (item.isMe) Color(0xFF3B6FE7) else Color.White,
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .border(
                                            width = if (item.isMe) 0.dp else 1.dp,
                                            color = Color(0xFFE2E8F0),
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = item.text,
                                            color = if (item.isMe) Color.White else Color(0xFF1E293B),
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.time,
                                            fontSize = 10.sp,
                                            color = if (item.isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF94A3B8),
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is DirectChatMessageItem.RoomCardMsg -> {
                        // Room card bubble
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFCBD5E1))
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .width(260.dp)
                                    .background(Color.White, RoundedCornerShape(18.dp))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    // Map Preview Placeholder
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .background(Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Bản đồ vị trí phòng 📍", fontSize = 12.sp, color = Color(0xFF64748B))
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(item.address, fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(item.time, fontSize = 10.sp, color = Color(0xFF94A3B8), modifier = Modifier.align(Alignment.End))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = Color(0xFF0F172A)),
        decorationBox = decorationBox
    )
}

@Preview(showBackground = true)
@Composable
fun DirectChatScreenPreview() {
    HomEverTheme {
        DirectChatScreen()
    }
}

package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.HomEverTheme

val CustomBluePrimary = Color(0xFF2962FF)
private val OrangePrimary = Color(0xFFF97316)

private val UndoIcon: ImageVector
    get() = ImageVector.Builder(name = "Undo", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.5f, 8f); curveTo(9.85f, 8f, 7.45f, 8.99f, 5.6f, 10.6f); lineTo(2f, 7f); lineTo(2f, 16f); lineTo(11f, 16f); lineTo(7.38f, 12.38f); curveTo(8.77f, 11.22f, 10.54f, 10.5f, 12.5f, 10.5f); curveTo(16.04f, 10.5f, 19.05f, 12.81f, 20.1f, 16f); lineTo(22.47f, 15.22f); curveTo(21.08f, 11.03f, 17.15f, 8f, 12.5f, 8f); close()
        }
    }.build()

private val CloseIcon: ImageVector
    get() = ImageVector.Builder(name = "Close", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 6.41f); lineTo(17.59f, 5f); lineTo(12f, 10.59f); lineTo(6.41f, 5f); lineTo(5f, 6.41f); lineTo(10.59f, 12f); lineTo(5f, 17.59f); lineTo(6.41f, 19f); lineTo(12f, 13.41f); lineTo(17.59f, 19f); lineTo(19f, 17.59f); lineTo(13.41f, 12f); lineTo(19f, 6.41f); close()
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

val DiscoverHomeIcon: ImageVector
    get() = ImageVector.Builder(name = "Home", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

val DiscoverPersonIcon: ImageVector
    get() = ImageVector.Builder(name = "Person", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 12f); curveTo(14.21f, 12f, 16f, 10.21f, 16f, 8f); curveTo(16f, 5.79f, 14.21f, 4f, 12f, 4f); curveTo(9.79f, 4f, 8f, 5.79f, 8f, 8f); curveTo(8f, 10.21f, 9.79f, 12f, 12f, 12f); close(); moveTo(12f, 14f); curveTo(9.33f, 14f, 4f, 15.34f, 4f, 18f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 18f); curveTo(20f, 15.34f, 14.67f, 14f, 12f, 14f); close()
        }
    }.build()

@Composable
fun DiscoverScreen(modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = { AppBottomNavigation("discover") }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Placeholder for User Image
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Text("Ảnh người dùng", color = Color(0xFFA0AAB0), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            
            // Top Logo
            Text(
                text = "Homever",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CustomBluePrimary,
                modifier = Modifier.padding(24.dp).align(Alignment.TopStart)
            )
            
            // Gradient Overlay at Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text("Trần Thu Hà, 22", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Sinh viên ĐH Ngoại Thương", fontSize = 14.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Thủ Đức • 3 - 4 triệu/tháng", fontSize = 14.sp, color = Color.White)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TagPill("Cú đêm")
                        TagPill("Gọn gàng")
                        TagPill("Không hút thuốc")
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActionButton(icon = UndoIcon, size = 48.dp, iconColor = Color.Black, bgColor = Color.White, label = "Quay lại")
                        ActionButton(icon = CloseIcon, size = 64.dp, iconColor = Color.Black, bgColor = Color.White, label = "Bỏ qua")
                        ActionButton(icon = HeartIcon, size = 64.dp, iconColor = Color.White, bgColor = OrangePrimary, label = "Quan tâm")
                        ActionButton(icon = ChatIcon, size = 48.dp, iconColor = Color.White, bgColor = CustomBluePrimary, label = "Nhắn tin")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun TagPill(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color(0xFF1F2937), fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionButton(icon: ImageVector, size: Dp, iconColor: Color, bgColor: Color, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(size * 0.45f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AppBottomNavigation(selectedItem: String) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(FlameIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CustomBluePrimary, selectedTextColor = CustomBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(DiscoverHomeIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CustomBluePrimary, selectedTextColor = CustomBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(HeartIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CustomBluePrimary, selectedTextColor = CustomBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(ChatIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CustomBluePrimary, selectedTextColor = CustomBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
        NavigationBarItem(
            icon = { Icon(DiscoverPersonIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = {},
            colors = NavigationBarItemDefaults.colors(selectedIconColor = CustomBluePrimary, selectedTextColor = CustomBluePrimary, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    HomEverTheme {
        DiscoverScreen()
    }
}
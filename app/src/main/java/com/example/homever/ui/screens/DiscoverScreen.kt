package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.homever.data.network.NetworkModule
import com.example.homever.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

data class RoommateCandidate(
    val name: String,
    val age: Int,
    val occupation: String,
    val location: String,
    val budget: String,
    val tags: List<String>,
    val bio: String,
    val matchPercentage: Int = 85,
    val gradientColors: List<Color>
)

val sampleCandidates = listOf(
    RoommateCandidate(
        name = "Trần Thu Hà",
        age = 22,
        occupation = "Sinh viên ĐH Ngoại Thương",
        location = "Thủ Đức, TP.HCM",
        budget = "3 - 4 triệu/tháng",
        tags = listOf("Cú đêm", "Gọn gàng", "Không hút thuốc"),
        bio = "Thích không gian yên tĩnh học tập, nấu ăn cuối tuần.",
        gradientColors = listOf(Color(0xFF3B82F6), Color(0xFF1E3A8A))
    ),
    RoommateCandidate(
        name = "Nguyễn Hoàng Nam",
        age = 24,
        occupation = "Software Engineer",
        location = "Quận 1, TP.HCM",
        budget = "4 - 6 triệu/tháng",
        tags = listOf("Dậy sớm", "Rất sạch sẽ", "Yêu động vật"),
        bio = "Làm việc hybrid, hòa đồng, ngăn nắp và tôn trọng riêng tư.",
        gradientColors = listOf(Color(0xFF10B981), Color(0xFF065F46))
    ),
    RoommateCandidate(
        name = "Lê Thảo My",
        age = 21,
        occupation = "Sinh viên ĐH Bách Khoa",
        location = "Quận 10, TP.HCM",
        budget = "2.5 - 3.5 triệu/tháng",
        tags = listOf("Thích yên tĩnh", "Sống ngăn nắp", "Thích nấu ăn"),
        bio = "Mong tìm bạn cùng phòng vui vẻ, tử tế để ở lâu dài.",
        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95))
    ),
    RoommateCandidate(
        name = "Phạm Quốc Bảo",
        age = 25,
        occupation = "Graphic Designer",
        location = "Bình Thạnh, TP.HCM",
        budget = "3.5 - 5 triệu/tháng",
        tags = listOf("Linh hoạt", "Cởi mở", "Thích âm nhạc"),
        bio = "Hay vẽ tranh và nghe nhạc nhẹ nhàng, gọn gàng văn minh.",
        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFF92400E))
    )
)

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    var candidates by remember { mutableStateOf(sampleCandidates) }
    var currentIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = NetworkModule.api.getMatchingCandidates(limit = 20)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (!body.isNullOrEmpty()) {
                        val mapped = body.mapIndexed { idx, item ->
                            val p = item.profile
                            val colorPalettes = listOf(
                                listOf(Color(0xFF3B82F6), Color(0xFF1E3A8A)),
                                listOf(Color(0xFF10B981), Color(0xFF065F46)),
                                listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95)),
                                listOf(Color(0xFFF59E0B), Color(0xFF92400E))
                            )
                            val tagsList = mutableListOf<String>()
                            if (!p.lifestyle.isNullOrBlank()) tagsList.addAll(p.lifestyle.split(";", ",").map { it.trim() }.filter { it.isNotBlank() })
                            if (!p.traits.isNullOrBlank()) tagsList.addAll(p.traits.split(";", ",").map { it.trim() }.filter { it.isNotBlank() })
                            if (tagsList.isEmpty()) tagsList.addAll(listOf("Linh hoạt", "Thân thiện"))

                            RoommateCandidate(
                                name = p.displayName.ifBlank { "Ứng viên ${idx + 1}" },
                                age = if (p.age > 0) p.age else 22,
                                occupation = p.occupation.ifBlank { "Sinh viên / Đi làm" },
                                location = p.location ?: "TP.HCM",
                                budget = p.budget ?: "Thỏa thuận",
                                tags = tagsList.take(3),
                                bio = p.bio ?: "Tìm bạn cùng phòng hòa đồng, tôn trọng không gian chung.",
                                matchPercentage = item.score.percentage,
                                gradientColors = colorPalettes[idx % colorPalettes.size]
                            )
                        }
                        withContext(Dispatchers.Main) {
                            candidates = mapped
                            currentIndex = 0
                            isLoading = false
                        }
                    }
                }
            } catch (e: Exception) {
                // Giữ sampleCandidates nếu lỗi hoặc offline
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    val candidate = candidates.getOrElse(currentIndex) { sampleCandidates[0] }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "discover",
                onDiscoverClick = {},
                onRoomClick = onNavigateToRoom,
                onMessagesClick = onNavigateToMessages,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Background Visual for Roommate Candidate
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(candidate.gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = DiscoverPersonIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = candidate.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Top Logo
            Text(
                text = "Homever",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(24.dp).align(Alignment.TopStart)
            )
            
            // Gradient Overlay at Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .align(Alignment.BottomCenter)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${candidate.name}, ${candidate.age}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF10B981), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "${candidate.matchPercentage}% Hợp",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(candidate.occupation, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("${candidate.location} • ${candidate.budget}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(candidate.bio, fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f), maxLines = 2)

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        candidate.tags.forEach { tag ->
                            TagPill(tag)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActionButton(
                            icon = UndoIcon,
                            size = 48.dp,
                            iconColor = Color.Black,
                            bgColor = Color.White,
                            label = "Quay lại",
                            onClick = {
                                currentIndex = (currentIndex - 1 + sampleCandidates.size) % sampleCandidates.size
                            }
                        )
                        ActionButton(
                            icon = CloseIcon,
                            size = 64.dp,
                            iconColor = Color.Black,
                            bgColor = Color.White,
                            label = "Bỏ qua",
                            onClick = {
                                currentIndex = (currentIndex + 1) % sampleCandidates.size
                            }
                        )
                        ActionButton(
                            icon = HeartIcon,
                            size = 64.dp,
                            iconColor = Color.White,
                            bgColor = OrangePrimary,
                            label = "Quan tâm",
                            onClick = {
                                currentIndex = (currentIndex + 1) % sampleCandidates.size
                            }
                        )
                        ActionButton(
                            icon = ChatIcon,
                            size = 48.dp,
                            iconColor = Color.White,
                            bgColor = BluePrimary,
                            label = "Nhắn tin",
                            onClick = {
                                onNavigateToMessages()
                            }
                        )
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
fun ActionButton(
    icon: ImageVector,
    size: Dp,
    iconColor: Color,
    bgColor: Color,
    label: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
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
fun AppBottomNavigation(
    selectedItem: String,
    onDiscoverClick: () -> Unit = {},
    onRoomClick: () -> Unit = {},
    onInvitesClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(FlameIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Khám phá", fontSize = 10.sp) },
            selected = selectedItem == "discover",
            onClick = onDiscoverClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(DiscoverHomeIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Phòng", fontSize = 10.sp) },
            selected = selectedItem == "room",
            onClick = onRoomClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(HeartIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Quan tâm", fontSize = 10.sp) },
            selected = selectedItem == "invites",
            onClick = onInvitesClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(ChatIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Tin nhắn", fontSize = 10.sp) },
            selected = selectedItem == "messages",
            onClick = onMessagesClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            icon = { Icon(DiscoverPersonIcon, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Hồ sơ", fontSize = 10.sp) },
            selected = selectedItem == "profile",
            onClick = onProfileClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.White
            )
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

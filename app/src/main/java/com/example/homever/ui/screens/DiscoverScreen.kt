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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.viewmodel.DiscoveryUiState
import com.example.homever.ui.theme.*

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
    val gradientColors: List<Color>,
    val avatarUrl: String? = null,
    val lifestyle: String? = null,
    val traits: String? = null,
    val needType: String? = null,
    val photos: List<String> = emptyList(),
    val id: String = "",
    val gender: String = ""
)

// Sample candidates with realistic high-quality photos (2 photos each)
// and elegant neutral fallback gradients (Slate / Charcoal / Warm Stone - NOT pure blue)
val sampleCandidates = listOf(
    RoommateCandidate(
        name = "Trần Thu Hà",
        age = 22,
        occupation = "Sinh viên ĐH Ngoại Thương",
        location = "Thủ Đức, TP.HCM",
        budget = "3 - 4 triệu/tháng",
        tags = listOf("Cú đêm", "Gọn gàng", "Không hút thuốc", "Thích nấu ăn", "Cần riêng tư", "Rất yên tĩnh", "Không nuôi thú cưng", "Thỉnh thoảng tiếp khách", "Chia đều chi phí", "Tự giác việc chung"),
        bio = "Thích không gian yên tĩnh học tập, nấu ăn cuối tuần.",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1000&q=80,https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=1000&q=80",
        gradientColors = listOf(Color(0xFF334155), Color(0xFF1E293B))
    ),
    RoommateCandidate(
        name = "Nguyễn Hoàng Nam",
        age = 24,
        occupation = "Software Engineer",
        location = "Quận 1, TP.HCM",
        budget = "4 - 6 triệu/tháng",
        tags = listOf("Dậy sớm", "Rất sạch sẽ", "Yêu động vật", "Không hút thuốc", "Thoải mái riêng tư", "Nấu ăn cuối tuần", "Thích phân công", "Linh hoạt chi phí"),
        bio = "Làm việc hybrid, hòa đồng, ngăn nắp và tôn trọng riêng tư.",
        avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=1000&q=80,https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=1000&q=80",
        gradientColors = listOf(Color(0xFF374151), Color(0xFF111827))
    ),
    RoommateCandidate(
        name = "Lê Thảo My",
        age = 21,
        occupation = "Sinh viên ĐH Bách Khoa",
        location = "Quận 10, TP.HCM",
        budget = "2.5 - 3.5 triệu/tháng",
        tags = listOf("Thích yên tĩnh", "Sống ngăn nắp", "Thích nấu ăn", "Không hút thuốc", "Có thể ở cùng thú cưng", "Hiếm khi có khách", "Chia đều chi phí", "Rất chủ động việc chung"),
        bio = "Mong tìm bạn cùng phòng vui vẻ, tử tế để ở lâu dài.",
        avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=1000&q=80,https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=1000&q=80",
        gradientColors = listOf(Color(0xFF4A4E69), Color(0xFF22223B))
    ),
    RoommateCandidate(
        name = "Phạm Quốc Bảo",
        age = 25,
        occupation = "Graphic Designer",
        location = "Bình Thạnh, TP.HCM",
        budget = "3.5 - 5 triệu/tháng",
        tags = listOf("Linh hoạt", "Cởi mở", "Thích âm nhạc", "Cú đêm", "Khá gọn gàng", "Không hút thuốc", "Thích sinh hoạt chung", "Chủ yếu mua ngoài"),
        bio = "Hay vẽ tranh và nghe nhạc nhẹ nhàng, gọn gàng văn minh.",
        avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=1000&q=80,https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=1000&q=80",
        gradientColors = listOf(Color(0xFF57534E), Color(0xFF1C1917))
    )
)

/**
 * Determines the "page" count for the candidate card.
 * Page 0 = first photo + basic info + first 3 tags.
 * Page 1 = second photo (if available) OR same photo + extended tags (up to 10) + bio.
 * The user taps the RIGHT half to go forward, LEFT half to go back.
 */
@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    state: DiscoveryUiState = DiscoveryUiState(),
    onRefresh: () -> Unit = {},
    onCompleteMatching: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSkip: () -> Unit = {},
    onInterest: (String) -> Unit = {},
    onPageChange: (Int) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToInvites: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToUserDetail: (RoommateCandidate) -> Unit = {}
) {
    val candidates = remember(state.candidates) {
        state.candidates.map { it.profile.asRoommateCandidate(it.score.percentage) }
    }
    val currentIndex = state.currentIndex
    val isLoading = state.isLoading

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "discover",
                onDiscoverClick = {},
                onRoomClick = onNavigateToRoom,
                onInvitesClick = onNavigateToInvites,
                onMessagesClick = onNavigateToMessages,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { innerPadding ->
        val candidate = candidates.getOrNull(currentIndex)
        if (candidate == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(state.errorMessage ?: if (candidates.isEmpty()) {
                        "Chưa có hồ sơ đáp ứng lựa chọn giới tính, khu vực và ngân sách của bạn."
                    } else {
                        "Bạn đã xem hết hồ sơ phù hợp hiện có."
                    })
                    Spacer(Modifier.height(16.dp))
                    if (state.profileIncomplete) {
                        Button(onClick = onCompleteMatching) { Text("Hoàn tất lựa chọn") }
                    } else {
                        Button(onClick = onRefresh) { Text("Tải lại") }
                        TextButton(onClick = onCompleteMatching) { Text("Điều chỉnh lựa chọn tìm kiếm") }
                    }
                    if (currentIndex > 0) {
                        TextButton(onClick = onPrevious) { Text("Xem thẻ trước") }
                    }
                }
            }
            return@Scaffold
        }
        ProfileDiscoveryCard(
            candidate = candidate,
            page = state.cardPage,
            onPageChange = onPageChange,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            onOpenProfile = { onNavigateToUserDetail(candidate) },
            errorMessage = state.errorMessage
        ) {
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
                        onPrevious()
                    }
                )
                ActionButton(
                    icon = CloseIcon,
                    size = 64.dp,
                    iconColor = Color.Black,
                    bgColor = Color.White,
                    label = "Bỏ qua",
                    onClick = {
                        onSkip()
                    }
                )
                ActionButton(
                    icon = HeartIcon,
                    size = 64.dp,
                    iconColor = Color.White,
                    bgColor = OrangePrimary,
                    label = if (state.sendingProfileId == candidate.id) "Đang gửi…" else "Quan tâm",
                    enabled = state.sendingProfileId == null,
                    onClick = { onInterest(candidate.id) }
                )
                ActionButton(
                    icon = ChatIcon,
                    size = 48.dp,
                    iconColor = Color.White,
                    bgColor = BluePrimary,
                    label = "Nhắn tin",
                    onClick = { onNavigateToMessages() }
                )
            }
        }
    }
}

@Composable
fun TagPill(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF1F2937),
            fontWeight = FontWeight.Medium,
            maxLines = 3
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    size: Dp,
    iconColor: Color,
    bgColor: Color,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = enabled) { onClick() }
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
    Column(Modifier.background(Color.White).navigationBarsPadding()) {
        NavigationBar(
            modifier = Modifier.height(64.dp),
            containerColor = Color.White,
            tonalElevation = 8.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
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
                icon = {
                    val unread = LocalChatUnreadCount.current
                    BadgedBox(badge = {
                        if (unread > 0) Badge { Text(if (unread > 99) "99+" else unread.toString()) }
                    }) {
                        Icon(ChatIcon, contentDescription = if (unread > 0) "$unread tin nhắn chưa đọc" else "Tin nhắn", modifier = Modifier.size(24.dp))
                    }
                },
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
}

@Preview(showBackground = true)
@Composable
fun DiscoverScreenPreview() {
    HomEverTheme {
        DiscoverScreen(
            state = DiscoveryUiState(
                candidates = sampleCandidates.mapIndexed { index, candidate ->
                    com.example.homever.data.network.CandidateResponse(
                        com.example.homever.data.network.UserProfile(
                            id = "preview-$index",
                            displayName = candidate.name,
                            age = candidate.age,
                            occupation = candidate.occupation,
                            location = candidate.location,
                            budget = candidate.budget,
                            lifestyle = candidate.tags.joinToString("; "),
                            bio = candidate.bio,
                            avatarUrl = candidate.avatarUrl
                        ),
                        com.example.homever.data.network.MatchingScore(percentage = candidate.matchPercentage)
                    )
                },
                candidatesLoaded = true
            )
        )
    }
}

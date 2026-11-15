package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.ui.theme.HomEverTheme

private val DetailArrowBackIcon: ImageVector
    get() = ImageVector.Builder(name = "ArrowBack", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 11f); lineTo(7.83f, 11f); lineTo(13.41f, 5.41f); lineTo(12f, 4f)
            lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
            lineTo(20f, 13f); close()
        }
    }.build()

private val ChatIconWhite: ImageVector
    get() = ImageVector.Builder(name = "Chat", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color.White)) {
            moveTo(20f, 2f); lineTo(4f, 2f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); lineTo(2f, 22f); lineTo(6f, 18f); lineTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); lineTo(22f, 4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close()
        }
    }.build()

private val SparkleIconSmall: ImageVector
    get() = ImageVector.Builder(name = "Sparkle", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF2563EB))) {
            moveTo(12f, 2f); lineTo(14.5f, 9.5f); lineTo(22f, 12f); lineTo(14.5f, 14.5f); lineTo(12f, 22f); lineTo(9.5f, 14.5f); lineTo(2f, 12f); lineTo(9.5f, 9.5f); close()
        }
    }.build()

private val HouseIconSmall: ImageVector
    get() = ImageVector.Builder(name = "House", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFF059669))) {
            moveTo(10f, 20f); lineTo(14f, 20f); lineTo(14f, 14f); lineTo(10f, 14f); close(); moveTo(21f, 10.27f); lineTo(12f, 3f); lineTo(3f, 10.27f); lineTo(3f, 21f); lineTo(8f, 21f); lineTo(8f, 12f); lineTo(16f, 12f); lineTo(16f, 21f); lineTo(21f, 21f); close()
        }
    }.build()

private val PeopleIconSmall: ImageVector
    get() = ImageVector.Builder(name = "People", defaultWidth = 16.dp, defaultHeight = 16.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
        path(fill = SolidColor(Color(0xFFD97706))) {
            moveTo(16f, 11f); curveTo(17.66f, 11f, 18.99f, 9.66f, 18.99f, 8f); curveTo(18.99f, 6.34f, 17.66f, 5f, 16f, 5f); curveTo(14.34f, 5f, 13f, 6.34f, 13f, 8f); curveTo(13f, 9.66f, 14.34f, 11f, 16f, 11f); close()
            moveTo(8f, 11f); curveTo(9.66f, 11f, 10.99f, 9.66f, 10.99f, 8f); curveTo(10.99f, 6.34f, 9.66f, 5f, 8f, 5f); curveTo(6.34f, 5f, 5f, 6.34f, 5f, 8f); curveTo(5f, 9.66f, 6.34f, 11f, 8f, 11f); close()
            moveTo(8f, 13f); curveTo(5.67f, 13f, 1f, 14.17f, 1f, 16.5f); lineTo(1f, 19f); lineTo(15f, 19f); lineTo(15f, 16.5f); curveTo(15f, 14.17f, 10.33f, 13f, 8f, 13f); close()
        }
    }.build()

@Composable
fun UserProfileDetailScreen(
    modifier: Modifier = Modifier,
    name: String = "Phạm Hữu Lầu",
    age: Int = 20,
    occupation: String = "",
    location: String = "Thủ Đức",
    budget: String = "3–5 triệu/tháng",
    bio: String = "",
    lifestyle: String = "",
    traits: String = "",
    tags: List<String> = emptyList(),
    avatarUrl: String? = null,
    matchPercentage: Int = 85,
    onBack: () -> Unit = {},
    onStartChat: () -> Unit = {},
    onReportUser: () -> Unit = {}
) {
    val locationAndBudget = listOf(location.ifBlank { "TP.HCM" }, budget.ifBlank { "Thỏa thuận" }).joinToString(" • ")
    val photos = remember(avatarUrl) {
        (avatarUrl ?: "").split(",").map { it.trim() }.filter { it.isNotBlank() }
    }
    var currentPhotoIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        // Top Bar: Back button + Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    .background(Color.White, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = DetailArrowBackIcon,
                    contentDescription = "Quay lại",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF1E293B)
                )
            }

            Text(
                text = "Hồ sơ người dùng",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable content wrapper
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Photo Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                if (photos.isNotEmpty()) {
                    val activePhoto = photos.getOrElse(currentPhotoIndex) { photos[0] }
                    AsyncImage(
                        model = activePhoto,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Touch areas to switch photos: Left half for previous, Right half for next
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    if (photos.size > 1) {
                                        currentPhotoIndex = (currentPhotoIndex - 1 + photos.size) % photos.size
                                    }
                                }
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    if (photos.size > 1) {
                                        currentPhotoIndex = (currentPhotoIndex + 1) % photos.size
                                    }
                                }
                        )
                    }

                    // Photo indicator dashes at top
                    if (photos.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 14.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            photos.indices.forEach { idx ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (idx == currentPhotoIndex) Color.White else Color.White.copy(alpha = 0.4f)
                                        )
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF3B82F6), Color(0xFF1E3A8A))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.take(1).uppercase().ifBlank { "U" },
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Dark gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "$name, $age",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF10B981), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$matchPercentage% Hợp",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (occupation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = occupation,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = locationAndBudget,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Intro Notes Section: Giới thiệu của tôi
            Text(
                text = "Giới thiệu của $name",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Note 1: Giới thiệu bản thân
            DetailNoteCard(
                title = "Giới thiệu bản thân",
                content = bio.ifBlank { "Chưa cập nhật phần giới thiệu bản thân." },
                iconBg = Color(0xFFDBEAFE),
                icon = SparkleIconSmall,
                cardBg = Color(0xFFF0F7FF)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Note 2: Ở cùng mình sẽ như thế nào?
            DetailNoteCard(
                title = "Ở cùng mình sẽ như thế nào?",
                content = lifestyle.ifBlank { "Chưa cập nhật phong cách sống cùng phòng." },
                iconBg = Color(0xFFDCFCE7),
                icon = HouseIconSmall,
                cardBg = Color(0xFFF0FDF4)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Note 3: Roommate mong muốn
            DetailNoteCard(
                title = "Roommate mong muốn",
                content = traits.ifBlank { "Chưa cập nhật tiêu chí roommate mong muốn." },
                iconBg = Color(0xFFFFEDD5),
                icon = PeopleIconSmall,
                cardBg = Color(0xFFFFF7ED)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Tags list
            val displayTags = if (tags.isNotEmpty()) tags else listOf("Thân thiện", "Gọn gàng", "Tôn trọng riêng tư")
            Text(
                text = "Thói quen & Phong cách sống",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                displayTags.take(4).forEach { tag ->
                    PreviewTagPill(tag)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button: Nhắn tin ngay
            Button(
                onClick = onStartChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = ChatIconWhite, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nhắn tin ngay", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailNoteCard(
    title: String,
    content: String,
    iconBg: Color,
    icon: ImageVector,
    cardBg: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun PreviewTagPill(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF1E293B),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailScreenPreview() {
    HomEverTheme {
        UserProfileDetailScreen(
            name = "Phạm Hữu Lầu",
            age = 22,
            bio = "Thích đọc sách, nghe nhạc nhẹ và giữ vệ sinh chung.",
            lifestyle = "Dậy sớm, rất sạch sẽ, không hút thuốc.",
            traits = "Tìm bạn cùng phòng hòa đồng, lịch sự và tử tế."
        )
    }
}

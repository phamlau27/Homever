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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.homever.ui.theme.*

private val ProfSettingsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Settings", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(HomeverTextGray)) {
            moveTo(19.14f, 12.94f); curveTo(19.19f, 12.63f, 19.22f, 12.32f, 19.22f, 12f)
            curveTo(19.22f, 11.68f, 19.19f, 11.37f, 19.14f, 11.06f); lineTo(21.16f, 9.48f)
            curveTo(21.34f, 9.34f, 21.39f, 9.08f, 21.28f, 8.87f); lineTo(19.36f, 5.55f)
            curveTo(19.25f, 5.34f, 18.99f, 5.25f, 18.78f, 5.34f); lineTo(16.4f, 6.29f)
            curveTo(15.91f, 5.92f, 15.37f, 5.6f, 14.79f, 5.36f); lineTo(14.43f, 2.82f)
            curveTo(14.4f, 2.59f, 14.2f, 2.42f, 13.96f, 2.42f); lineTo(10.04f, 2.42f)
            curveTo(9.8f, 2.42f, 9.6f, 2.59f, 9.57f, 2.82f); lineTo(9.21f, 5.36f)
            curveTo(8.63f, 5.6f, 8.09f, 5.92f, 7.6f, 6.29f); lineTo(5.22f, 5.34f)
            curveTo(5.01f, 5.25f, 4.75f, 5.34f, 4.64f, 5.55f); lineTo(2.72f, 8.87f)
            curveTo(2.61f, 9.08f, 2.66f, 9.34f, 2.84f, 9.48f); lineTo(4.86f, 11.06f)
            curveTo(4.81f, 11.37f, 4.78f, 11.68f, 4.78f, 12f); curveTo(4.78f, 12.32f, 4.81f, 12.63f, 4.86f, 12.94f)
            lineTo(2.84f, 14.52f); curveTo(2.66f, 14.66f, 2.61f, 14.92f, 2.72f, 15.13f)
            lineTo(4.64f, 18.45f); curveTo(4.75f, 18.66f, 5.01f, 18.75f, 5.22f, 18.66f)
            lineTo(7.6f, 17.71f); curveTo(8.09f, 18.08f, 8.63f, 18.4f, 9.21f, 18.64f)
            lineTo(9.57f, 21.18f); curveTo(9.6f, 21.41f, 9.8f, 21.58f, 10.04f, 21.58f)
            lineTo(13.96f, 21.58f); curveTo(14.2f, 21.58f, 14.4f, 21.41f, 14.43f, 21.18f)
            lineTo(14.79f, 18.64f); curveTo(15.37f, 18.4f, 15.91f, 18.08f, 16.4f, 17.71f)
            lineTo(18.78f, 18.66f); curveTo(18.99f, 18.75f, 19.25f, 18.66f, 19.36f, 18.45f)
            lineTo(21.28f, 15.13f); curveTo(21.39f, 14.92f, 21.34f, 14.66f, 21.16f, 14.52f)
            lineTo(19.14f, 12.94f); close()
            moveTo(12f, 15.5f); curveTo(10.07f, 15.5f, 8.5f, 13.93f, 8.5f, 12f)
            curveTo(8.5f, 10.07f, 10.07f, 8.5f, 12f, 8.5f); curveTo(13.93f, 8.5f, 15.5f, 10.07f, 15.5f, 12f)
            curveTo(15.5f, 13.93f, 13.93f, 15.5f, 12f, 15.5f); close()
        }
    }.build()

@Composable
fun PersonalProfileScreen(
    displayName: String = "Tôi",
    gender: String = "",
    age: String = "",
    occupation: String = "",
    needType: String = "",
    lifestyle: String = "",
    budget: String = "",
    location: String = "",
    bio: String = "",
    avatarUrl: String? = null,
    onSettingsClick: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    val subtitleInfo = listOfNotNull(
        if (age.isNotBlank()) "$age tuổi" else null,
        gender.ifBlank { null },
        occupation.ifBlank { null }
    ).joinToString(" • ")

    val displayNeed = when (needType) {
        "room" -> "Tôi cần tìm phòng"
        "roommate" -> "Tôi tìm bạn cùng phòng"
        else -> needType.ifBlank { "Chưa cập nhật nhu cầu" }
    }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "profile",
                onProfileClick = {},
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onMessagesClick = onNavigateToMessages
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGray)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hồ sơ của tôi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, HomeverGrayBorder, CircleShape)
                        .background(Color.White, CircleShape)
                        .clickable { onSettingsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ProfSettingsIcon,
                        contentDescription = "Cài đặt tài khoản",
                        modifier = Modifier.size(20.dp),
                        tint = HomeverTextGray
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                // Avatar + Header Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(BluePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!avatarUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = displayName.take(1).uppercase().ifBlank { "U" },
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = displayName.ifBlank { "Người dùng" },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = HomeverTextDark
                        )
                        if (subtitleInfo.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = subtitleInfo,
                                fontSize = 13.sp,
                                color = TextGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Card 1: Nhu cầu & Khu vực
                ProfCard(title = "Nhu cầu & Ngân sách") {
                    ProfDetailRow(label = "Nhu cầu chính", value = displayNeed)
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = HomeverGrayBorder)
                    Spacer(Modifier.height(10.dp))
                    ProfDetailRow(label = "Khoảng ngân sách", value = budget.ifBlank { "Chưa cập nhật" })
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = HomeverGrayBorder)
                    Spacer(Modifier.height(10.dp))
                    ProfDetailRow(label = "Khu vực mong muốn", value = location.ifBlank { "Chưa cập nhật" })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 2: Lối sống & Thói quen
                ProfCard(title = "Lối sống & Thói quen") {
                    val traitsList = lifestyle.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    if (traitsList.isNotEmpty()) {
                        traitsList.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { trait ->
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFEBF5FF), RoundedCornerShape(16.dp))
                                            .border(1.dp, Color(0xFFC7E2FE), RoundedCornerShape(16.dp))
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = trait,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = BluePrimary
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    } else {
                        Text(
                            text = "Chưa cập nhật thông tin lối sống.",
                            fontSize = 13.sp,
                            color = TextGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 3: Giới thiệu bản thân
                ProfCard(title = "Giới thiệu bản thân") {
                    Text(
                        text = bio.ifBlank { "Chưa cập nhật phần giới thiệu bản thân." },
                        fontSize = 13.sp,
                        color = if (bio.isBlank()) TextGray.copy(alpha = 0.6f) else HomeverTextDark,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ProfCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, HomeverGrayBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = HomeverTextDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ProfDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextGray
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomeverTextDark
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalProfileScreenPreview() {
    HomEverTheme {
        PersonalProfileScreen(
            displayName = "Nguyễn Văn A",
            gender = "Nam",
            age = "22",
            occupation = "Sinh viên ĐH Bách Khoa",
            needType = "roommate",
            lifestyle = "Dậy sớm, Khá yên tĩnh, Rất gọn gàng, Khói thuốc: Không hút",
            budget = "3.000.000 - 5.000.000 đ",
            location = "Quận 1, TP. Hồ Chí Minh",
            bio = "Thích đọc sách, giữ gìn vệ sinh chung, tôn trọng không gian riêng."
        )
    }
}
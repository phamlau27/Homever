package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.model.LifestyleCriteria
import com.example.homever.ui.theme.HomEverTheme

@Composable
fun UserProfileDetailScreen(
    modifier: Modifier = Modifier,
    name: String = "Người dùng",
    age: Int = 0,
    occupation: String = "",
    location: String = "",
    budget: String = "",
    bio: String = "",
    lifestyle: String = "",
    traits: String = "",
    tags: List<String> = emptyList(),
    avatarUrl: String? = null,
    matchPercentage: Int = 0,
    onBack: () -> Unit = {},
    onStartChat: () -> Unit = {},
    onReportUser: (() -> Unit)? = null,
    isOwnPreview: Boolean = false,
    initialPage: Int = 0,
    photos: List<String> = emptyList(),
    gender: String = ""
) {
    var page by rememberSaveable(name, age, avatarUrl, isOwnPreview) { mutableIntStateOf(initialPage) }
    val candidate = UserProfile(
        displayName = name,
        gender = gender,
        age = age,
        occupation = occupation,
        location = location,
        budget = budget,
        bio = bio,
        lifestyle = lifestyle,
        traits = traits,
        avatarUrl = avatarUrl
    ).asRoommateCandidate(matchPercentage).let { profile ->
        profile.copy(
            tags = (profile.tags + LifestyleCriteria.publicLabels(tags.joinToString(";"))).distinct(),
            photos = photos
        )
    }

    ProfileDiscoveryCard(
        candidate = candidate,
        page = page,
        onPageChange = { page = it },
        modifier = modifier.background(Color.Black).navigationBarsPadding(),
        title = if (isOwnPreview) "Xem trước hồ sơ" else "Homever",
        onBack = onBack
    ) {
        if (!isOwnPreview) {
            Button(
                onClick = onStartChat,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Nhắn tin ngay")
            }
            if (onReportUser != null) {
                TextButton(onClick = onReportUser) {
                    Text("Báo cáo người dùng", color = Color.White)
                }
            }
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
            lifestyle = "Hút thuốc: Không hút; Riêng tư: Cân bằng",
            isOwnPreview = true
        )
    }
}

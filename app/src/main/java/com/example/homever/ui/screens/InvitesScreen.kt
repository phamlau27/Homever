package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.homever.data.network.UserInterest
import com.example.homever.data.network.UserProfile
import com.example.homever.domain.model.LifestyleCriteria
import com.example.homever.ui.theme.BluePrimary
import com.example.homever.viewmodel.DiscoveryUiState

@Composable
fun InvitesScreen(
    modifier: Modifier = Modifier,
    initialTab: Int = 0,
    state: DiscoveryUiState = DiscoveryUiState(),
    onRetry: () -> Unit = {},
    onInterest: (String) -> Unit = {},
    onViewProfile: (UserProfile) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(initialTab) }
    val entries = if (selectedTab == 0) state.interests.received else state.interests.sent
    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "invites",
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onMessagesClick = onNavigateToMessages,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize().background(Color(0xFFF9FAFB)).padding(innerPadding)
        ) {
            Text(
                "Lượt quan tâm",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary,
                modifier = Modifier.padding(24.dp)
            )
            TabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    text = { Text("Đã nhận (${state.interests.received.size})") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    text = { Text("Đã gửi (${state.interests.sent.size})") })
            }
            if (state.interestsLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            state.interestsError?.let { message ->
                Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                TextButton(onClick = onRetry, enabled = !state.interestsLoading) { Text("Thử lại") }
            }
            if (entries.isEmpty() && !state.interestsLoading && state.interestsError == null) {
                Text(
                    if (selectedTab == 0) "Chưa có lượt quan tâm nào được nhận." else "Bạn chưa gửi quan tâm. Hãy khám phá và chọn người phù hợp.",
                    modifier = Modifier.padding(24.dp),
                    color = Color.Gray
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries, key = { it.id }) { interest ->
                    InterestCard(
                        interest = interest,
                        received = selectedTab == 0,
                        sending = state.sendingProfileId != null,
                        onInterest = { onInterest(interest.profile.id) },
                        onViewProfile = { onViewProfile(interest.profile) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InterestCard(
    interest: UserInterest,
    received: Boolean,
    sending: Boolean,
    onInterest: () -> Unit,
    onViewProfile: () -> Unit
) {
    val profile = interest.profile
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onViewProfile),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val photo = profile.avatarUrl.orEmpty().split(",").firstOrNull { it.isNotBlank() }
                if (photo != null) {
                    AsyncImage(
                        model = photo.trim(),
                        contentDescription = profile.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(52.dp).clip(CircleShape)
                    )
                } else {
                    Box(Modifier.size(52.dp).background(Color(0xFFE5E7EB), CircleShape), contentAlignment = Alignment.Center) {
                        Text(profile.displayName.take(1))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("${profile.displayName}, ${profile.age}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(profile.occupation, fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(listOfNotNull(profile.location, profile.budget).filter(String::isNotBlank).joinToString(" · "), fontSize = 13.sp)
            FlowRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LifestyleCriteria.profileLabels(profile.lifestyle, profile.gender).forEach { tag ->
                    Surface(color = Color(0xFFF3F4F6), shape = RoundedCornerShape(12.dp)) {
                        Text(tag, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    }
                }
            }
            if (received && !interest.mutual) {
                Button(onClick = onInterest, enabled = !sending, modifier = Modifier.align(Alignment.End)) {
                    Text(if (sending) "Đang gửi…" else "Quan tâm")
                }
            } else {
                Text(
                    if (interest.mutual) "Đã cùng quan tâm" else "Đang chờ phản hồi",
                    color = if (interest.mutual) Color(0xFF059669) else Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

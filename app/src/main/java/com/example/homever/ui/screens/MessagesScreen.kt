package com.example.homever.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.data.network.ConversationSummary
import com.example.homever.ui.theme.*
import com.example.homever.viewmodel.ChatUiState

@Composable
fun MessagesScreen(
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToRoom: () -> Unit = {},
    onNavigateToInvites: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    state: ChatUiState = ChatUiState(),
    onOpenConversation: (String) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    val conversations = state.conversations.filter {
        it.profile.displayName.contains(query, ignoreCase = true) ||
            it.lastMessage?.content?.contains(query, ignoreCase = true) == true
    }
    val newConnections = conversations.filter { it.lastMessage == null }
    val recent = conversations.filter { it.lastMessage != null }
    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                selectedItem = "messages",
                onDiscoverClick = onNavigateToDiscover,
                onRoomClick = onNavigateToRoom,
                onInvitesClick = onNavigateToInvites,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().consumeWindowInsets(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = padding.calculateTopPadding() + 16.dp, bottom = padding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Tin nhắn", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BluePrimary) }
            item {
                OutlinedTextField(value = query, onValueChange = { query = it }, placeholder = { Text("Tìm cuộc trò chuyện…") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp))
            }
            if (state.loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
            state.error?.let { message ->
                item {
                    Text(message, color = MaterialTheme.colorScheme.error)
                    TextButton(onClick = onRetry) { Text("Thử lại") }
                }
            }
            if (newConnections.isNotEmpty()) {
                item { Text("KẾT NỐI MỚI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray) }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(newConnections, key = { it.id }) { connection ->
                            Column(
                                Modifier.width(80.dp).clickable { onOpenConversation(connection.id) }.padding(vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ChatAvatar(connection.profile.displayName, connection.profile.avatarUrl, Modifier.size(60.dp))
                                Spacer(Modifier.height(6.dp))
                                Text(connection.profile.displayName, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
            if (recent.isNotEmpty()) {
                item { Text("TRÒ CHUYỆN GẦN ĐÂY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray) }
                items(recent, key = { it.id }) { conversation ->
                    ConversationRow(conversation) { onOpenConversation(conversation.id) }
                }
            }
            if (conversations.isEmpty() && !state.loading && state.error == null) {
                item {
                    Text(
                        if (query.isNotBlank()) "Không tìm thấy cuộc trò chuyện." else "Chưa có kết nối. Khi hai bạn cùng quan tâm, cuộc trò chuyện sẽ xuất hiện ở đây.",
                        color = TextGray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(conversation: ConversationSummary, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            ChatAvatar(conversation.profile.displayName, conversation.profile.avatarUrl, Modifier.size(50.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(conversation.profile.displayName, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(conversation.lastMessage?.content.orEmpty(), fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = TextGray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(chatTime(conversation.lastMessage?.createdAt), fontSize = 11.sp, color = TextGray)
                if (conversation.unreadCount > 0) {
                    Badge { Text(conversation.unreadCount.toString()) }
                }
            }
        }
    }
}

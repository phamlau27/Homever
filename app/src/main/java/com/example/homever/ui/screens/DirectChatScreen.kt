package com.example.homever.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homever.ui.theme.*
import com.example.homever.viewmodel.ChatUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(
    modifier: Modifier = Modifier,
    userName: String = "Cuộc trò chuyện",
    onBack: () -> Unit = {},
    onViewProfile: () -> Unit = {},
    avatarUrl: String? = null,
    state: ChatUiState = ChatUiState(),
    onDraftChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onLoadOlder: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.messages.lastOrNull()?.id) {
        if (state.messages.isNotEmpty() &&
            (listState.firstVisibleItemIndex < 2 || state.messages.last().senderId == state.currentProfileId)
        ) listState.animateScrollToItem(0)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(Modifier.clickable(onClick = onViewProfile), verticalAlignment = Alignment.CenterVertically) {
                        ChatAvatar(userName, avatarUrl, Modifier.size(40.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(userName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Đã kết nối", fontSize = 12.sp, color = TextGray)
                        }
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(ProfArrowBackIcon, contentDescription = "Quay lại") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding()
        ) {
            if (state.loadingMessages) LinearProgressIndicator(Modifier.fillMaxWidth())
            state.threadError?.let { message ->
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
                    TextButton(onClick = onRetry) { Text("Thử lại") }
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFF8FAFC)),
                state = listState,
                reverseLayout = true,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.messages.asReversed(), key = { it.id }) { message ->
                    val mine = message.senderId == state.currentProfileId
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (mine) Alignment.End else Alignment.Start) {
                        Surface(
                            modifier = Modifier.widthIn(max = 300.dp),
                            color = if (mine) BluePrimary else Color.White,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(message.content, color = if (mine) Color.White else HomeverTextDark, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp))
                        }
                        Text(chatTime(message.createdAt), fontSize = 10.sp, color = TextGray)
                    }
                }
                if (state.hasOlderMessages) {
                    item {
                        TextButton(onClick = onLoadOlder, enabled = !state.loadingOlder, modifier = Modifier.fillMaxWidth()) {
                            Text(if (state.loadingOlder) "Đang tải…" else "Tải tin nhắn cũ")
                        }
                    }
                }
                if (state.messages.isEmpty() && !state.loadingMessages && state.threadError == null) {
                    item { Text("Hai bạn đã kết nối. Hãy gửi lời chào!", color = TextGray, modifier = Modifier.padding(16.dp)) }
                }
            }
            state.sendError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) }
            Row(
                Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.draft,
                    onValueChange = onDraftChange,
                    placeholder = { Text("Nhập tin nhắn…") },
                    modifier = Modifier.weight(1f),
                    maxLines = 4,
                    shape = RoundedCornerShape(24.dp)
                )
                Button(onClick = onSend, enabled = state.draft.isNotBlank() && !state.sending) {
                    Text(if (state.sending) "Đang gửi" else "Gửi")
                }
            }
        }
    }
}

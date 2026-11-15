package com.example.homever.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homever.data.network.*
import com.example.homever.domain.model.mergeChatMessages
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

data class ChatUiState(
    val currentProfileId: String = "",
    val conversations: List<ConversationSummary> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val pendingMatch: ConversationSummary? = null,
    val activeConversationId: String? = null,
    val messages: List<ChatMessage> = emptyList(),
    val loadingMessages: Boolean = false,
    val loadingOlder: Boolean = false,
    val hasOlderMessages: Boolean = false,
    val sending: Boolean = false,
    val draft: String = "",
    val threadError: String? = null,
    val sendError: String? = null
)

class ChatViewModel(private val api: HomeverApi) : ViewModel() {
    private val mutableState = MutableStateFlow(ChatUiState())
    val state = mutableState.asStateFlow()
    private val listMutex = Mutex()
    private val threadMutex = Mutex()
    private val handledMatches = mutableSetOf<String>()
    private val pendingAcknowledgements = mutableSetOf<String>()
    private val drafts = mutableMapOf<String, String>()
    private val pendingSends = mutableMapOf<String, SendMessageRequest>()
    private var lastReadId = 0
    private var threadCursor: Int? = null
    private var generation = 0
    private var conversationRevision = 0
    private var conversationsLoaded = false
    private var threadLoaded = false

    suspend fun refreshConversations() = listMutex.withLock {
        val requestGeneration = generation
        val requestRevision = conversationRevision
        if (!conversationsLoaded) mutableState.update { it.copy(loading = true) }
        try {
            for (id in pendingAcknowledgements.toList()) {
                if (api.acknowledgeMatch(id).isSuccessful) pendingAcknowledgements.remove(id)
            }
            val response = api.getConversations()
            if (!response.isSuccessful) error("HTTP ${response.code()}")
            val result = response.body() ?: error("EMPTY_RESPONSE")
            if (requestGeneration != generation || requestRevision != conversationRevision) return@withLock
            conversationsLoaded = true
            mutableState.update {
                it.copy(
                    currentProfileId = result.currentProfileId,
                    conversations = result.conversations,
                    pendingMatch = result.conversations.firstOrNull { conversation ->
                        conversation.pendingMatch && conversation.id !in handledMatches
                    },
                    loading = false,
                    error = null
                )
            }
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            if (requestGeneration == generation && requestRevision == conversationRevision) mutableState.update {
                it.copy(loading = false, error = "Không tải được cuộc trò chuyện. Vui lòng thử lại.")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch { refreshConversations() }
    }

    fun acceptConnection(connection: ConversationSummary, ownProfileId: String) {
        conversationRevision++
        mutableState.update {
            it.copy(
                currentProfileId = ownProfileId.ifBlank { it.currentProfileId },
                loading = false,
                conversations = listOf(connection) + it.conversations.filterNot { existing -> existing.id == connection.id },
                pendingMatch = if (connection.pendingMatch && connection.id !in handledMatches) connection else it.pendingMatch
            )
        }
    }

    fun acknowledgeMatch(id: String) {
        handledMatches.add(id)
        pendingAcknowledgements.add(id)
        mutableState.update { it.copy(pendingMatch = null) }
        refresh()
    }

    fun openConversation(id: String) {
        if (mutableState.value.activeConversationId == id) return
        lastReadId = 0
        threadCursor = null
        threadLoaded = false
        mutableState.update {
            it.copy(
                activeConversationId = id,
                messages = emptyList(),
                draft = drafts[id].orEmpty(),
                loadingMessages = true,
                hasOlderMessages = false,
                threadError = null,
                sendError = null
            )
        }
    }

    fun closeConversation(id: String) {
        if (mutableState.value.activeConversationId == id) {
            mutableState.update { it.copy(activeConversationId = null) }
        }
    }

    fun updateDraft(value: String) {
        val id = mutableState.value.activeConversationId ?: return
        if (value.length > 4000) return
        drafts[id] = value
        mutableState.update { it.copy(draft = value, sendError = null) }
    }

    suspend fun refreshMessages(id: String) = threadMutex.withLock {
        if (mutableState.value.activeConversationId != id) return@withLock
        val threadGeneration = generation
        val initial = !threadLoaded
        if (initial) mutableState.update { it.copy(loadingMessages = true) }
        try {
            var cursor = threadCursor
            do {
                val response = api.getMessages(id, afterId = cursor)
                if (!response.isSuccessful) error("HTTP ${response.code()}")
                val result = response.body() ?: error("EMPTY_RESPONSE")
                if (threadGeneration != generation || mutableState.value.activeConversationId != id) return@withLock
                threadLoaded = true
                mutableState.update {
                    it.copy(
                        messages = mergeChatMessages(it.messages, result.messages),
                        hasOlderMessages = if (cursor == null) result.hasMore else it.hasOlderMessages,
                        loadingMessages = false,
                        threadError = null
                    )
                }
                val continueAfter = cursor != null && result.hasMore && result.messages.isNotEmpty()
                cursor = result.messages.lastOrNull()?.id ?: cursor
                threadCursor = cursor
            } while (continueAfter)
            val latest = threadCursor ?: 0
            if (latest > lastReadId && api.markMessagesRead(id, MarkReadRequest(latest)).isSuccessful) {
                if (threadGeneration != generation || mutableState.value.activeConversationId != id) return@withLock
                lastReadId = latest
                mutableState.update {
                    it.copy(conversations = it.conversations.map { conversation ->
                        if (conversation.id == id) conversation.copy(unreadCount = 0) else conversation
                    })
                }
            }
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            if (threadGeneration == generation && mutableState.value.activeConversationId == id) mutableState.update {
                it.copy(loadingMessages = false, threadError = "Không tải được tin nhắn. Hãy thử lại.")
            }
        }
    }

    fun retryMessages() {
        val id = mutableState.value.activeConversationId ?: return
        viewModelScope.launch { refreshMessages(id) }
    }

    fun loadOlderMessages() {
        val id = mutableState.value.activeConversationId ?: return
        if (mutableState.value.loadingOlder || !mutableState.value.hasOlderMessages) return
        mutableState.update { it.copy(loadingOlder = true) }
        viewModelScope.launch {
            threadMutex.withLock {
                try {
                    if (mutableState.value.activeConversationId != id) return@withLock
                    val before = mutableState.value.messages.firstOrNull()?.id ?: return@withLock
                    val response = api.getMessages(id, beforeId = before)
                    if (!response.isSuccessful) error("HTTP ${response.code()}")
                    val result = response.body() ?: error("EMPTY_RESPONSE")
                    if (mutableState.value.activeConversationId == id) mutableState.update {
                        it.copy(messages = mergeChatMessages(it.messages, result.messages), hasOlderMessages = result.hasMore, threadError = null)
                    }
                } catch (error: Exception) {
                    if (error is CancellationException) throw error
                    if (mutableState.value.activeConversationId == id) mutableState.update { it.copy(threadError = "Không tải được tin nhắn cũ.") }
                } finally {
                    if (mutableState.value.activeConversationId == id) mutableState.update { it.copy(loadingOlder = false) }
                }
            }
        }
    }

    fun sendMessage() {
        val current = mutableState.value
        val id = current.activeConversationId ?: return
        val content = current.draft.trim()
        if (current.sending || content.isBlank() || content.length > 4000) return
        val request = pendingSends[id]?.takeIf { it.content == content }
            ?: SendMessageRequest(content, UUID.randomUUID().toString())
        pendingSends[id] = request
        mutableState.update { it.copy(sending = true, sendError = null) }
        viewModelScope.launch {
            try {
                val response = api.sendMessage(id, request)
                if (!response.isSuccessful) error("HTTP ${response.code()}")
                val sent = response.body() ?: error("EMPTY_RESPONSE")
                pendingSends.remove(id)
                if (drafts[id]?.trim() == content) drafts[id] = ""
                if (mutableState.value.activeConversationId == id) mutableState.update {
                    it.copy(messages = mergeChatMessages(it.messages, listOf(sent)), draft = drafts[id].orEmpty(), sending = false)
                } else mutableState.update { it.copy(sending = false) }
                refreshConversations()
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update {
                    it.copy(sending = false, sendError = if (it.activeConversationId == id) "Chưa gửi được. Nhấn Gửi để thử lại." else it.sendError)
                }
            }
        }
    }

    fun reset() {
        generation++
        conversationRevision = 0
        conversationsLoaded = false
        threadLoaded = false
        threadCursor = null
        viewModelScope.coroutineContext.cancelChildren()
        handledMatches.clear()
        pendingAcknowledgements.clear()
        pendingSends.clear()
        drafts.clear()
        lastReadId = 0
        mutableState.value = ChatUiState()
    }
}

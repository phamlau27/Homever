package com.example.homever.domain.model

import com.example.homever.data.network.ChatMessage

fun mergeChatMessages(existing: List<ChatMessage>, incoming: List<ChatMessage>): List<ChatMessage> =
    (existing + incoming).associateBy { it.id }.values.sortedBy { it.id }

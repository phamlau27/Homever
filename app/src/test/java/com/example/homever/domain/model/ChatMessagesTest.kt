package com.example.homever.domain.model

import com.example.homever.data.network.ChatMessage
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatMessagesTest {
    private fun message(id: Int, content: String = "Tin $id") =
        ChatMessage(id, "conversation", "sender", "client-$id", content, "2026-11-15T10:00:00.000Z")

    @Test
    fun pollingAndSendResponsesDoNotDuplicateMessages() {
        val sent = message(3)
        assertEquals(listOf(message(1), message(2), sent), mergeChatMessages(listOf(message(1), sent), listOf(message(2), sent)))
    }

    @Test
    fun olderPagesPrependInStableServerOrder() {
        assertEquals(listOf(1, 2, 3, 4), mergeChatMessages(listOf(message(3), message(4)), listOf(message(2), message(1))).map { it.id })
    }

    @Test
    fun serverResponseReplacesTheSameMessageWithoutDroppingOtherMessages() {
        assertEquals(listOf(message(1), message(2, "Nội dung đã tải")), mergeChatMessages(listOf(message(1), message(2)), listOf(message(2, "Nội dung đã tải"))))
    }
}

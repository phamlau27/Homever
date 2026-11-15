package com.example.homever

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.homever.data.network.ChatMessage
import com.example.homever.data.network.ConversationSummary
import com.example.homever.data.network.UserProfile
import com.example.homever.ui.screens.DirectChatScreen
import com.example.homever.ui.screens.MatchScreen
import com.example.homever.ui.screens.MessagesScreen
import com.example.homever.ui.theme.HomEverTheme
import com.example.homever.viewmodel.ChatUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFlowUiTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun matchActionsInvokeTheChatAndDiscoverCallbacks() {
        var chatOpened = false
        var discoverOpened = false
        composeRule.setContent {
            HomEverTheme {
                MatchScreen(
                    otherName = "Bạn thử",
                    onStartChat = { chatOpened = true },
                    onContinueDiscover = { discoverOpened = true }
                )
            }
        }
        composeRule.onNodeWithText("Bạn thử").assertIsDisplayed()
        composeRule.onNodeWithText("Nhắn tin ngay").performClick()
        composeRule.onNodeWithText("Tiếp tục khám phá").performClick()
        composeRule.runOnIdle {
            assertTrue(chatOpened)
            assertTrue(discoverOpened)
        }
    }

    @Test
    fun newConnectionOpensItsActualConversationId() {
        var openedId = ""
        val connection = ConversationSummary(
            id = "conversation-test",
            profile = UserProfile(id = "peer-test", displayName = "Bạn thử"),
            createdAt = "2026-11-15T10:00:00.000Z"
        )
        composeRule.setContent {
            HomEverTheme {
                MessagesScreen(
                    state = ChatUiState(conversations = listOf(connection)),
                    onOpenConversation = { openedId = it }
                )
            }
        }
        composeRule.onNodeWithText("KẾT NỐI MỚI").assertIsDisplayed()
        composeRule.onNodeWithText("Bạn thử").performClick()
        composeRule.runOnIdle { assertEquals(connection.id, openedId) }
    }

    @Test
    fun focusedComposerRemainsVisibleAndSendsItsDraft() {
        var sentText = ""
        composeRule.setContent {
            var state by remember {
                mutableStateOf(
                    ChatUiState(
                        currentProfileId = "me",
                        activeConversationId = "conversation-test",
                        messages = listOf(ChatMessage(1, "conversation-test", "peer", "request-test", "Lời chào thử", "2026-11-15T10:00:00.000Z"))
                    )
                )
            }
            HomEverTheme {
                DirectChatScreen(
                    userName = "Bạn thử",
                    state = state,
                    onDraftChange = { state = state.copy(draft = it) },
                    onSend = {
                        sentText = state.draft
                        state = state.copy(draft = "")
                    }
                )
            }
        }
        composeRule.onNodeWithText("Lời chào thử").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).performClick().performTextInput("Xin chào")
        composeRule.onNode(hasSetTextAction()).assertIsFocused().assertIsDisplayed()
        composeRule.onNodeWithText("Gửi").assertIsDisplayed().assertIsEnabled().performClick()
        composeRule.runOnIdle { assertEquals("Xin chào", sentText) }
    }
}

package ru.netology

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest

class ChatServiceTest {

    val cs = ChatService
    @BeforeTest
    fun reinit() {
        cs.reInit()
        // fill messages
        cs.message(1,2, "test message")
        cs.message(2, 1, "test message 2")
        cs.message(3, 1, "new chat2")
        cs.message(1,3, "answer to in chat2")
        cs.message(1,3, "answer to in chat3")
        cs.message(1,3, "answer to in chat4")
        cs.message(1,3, "answer to in chat5")
    }
    @Test
    fun message() {
        assertNotEquals(0, cs.getLastMessageId())
    }

    @Test
    fun getChatList() {
        assertNotEquals(0, cs.getChatList().size)
    }

    @Test
    fun getMessages() {
        val messageList = cs.getMessages(2)
        assertNotNull(messageList)
    }

    @Test
    fun deleteChat() {
        cs.deleteChat(2)
        assertNull(cs.getMessages(2))
    }

    @Test
    fun deleteMessage() {
        cs.deleteMessage(2, 7)
        val messageList = cs.getMessages(2)
        assertEquals(0, messageList?.filter { it.messageId == 7.toLong() }?.size)
    }

    @Test
    fun getUnreadedChatsCount() {
        assertNotEquals(0, cs.getUnreadedChatsCount())
    }
}
package ru.netology

data class Chat(
    val chatId: Long,
    val recipients: Set<Int> = mutableSetOf(),
    val messageList: MutableList<ChatMessage>
)

data class ChatMessage (
    val messageId: Long,
    val from: Int,
    val text: String,
    var isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
): Comparable<ChatMessage>
{
    override fun compareTo(other: ChatMessage): Int = timestamp.compareTo(other.timestamp)
}

fun <E> Iterable<Chat>.fromTo(from: Int, to: Int): Chat? {
    return try {
        filter(fun(chat: Chat) = chat.recipients.containsAll(listOf(from, to)))[0]
    } catch (e: IndexOutOfBoundsException)
    {
        null
    }
}

fun <E> Iterable<Chat>.byId(chatId: Long): MutableList<ChatMessage>? {
    return try {
        filter(fun(chat: Chat) = chat.chatId == chatId)[0].messageList
    } catch (e: IndexOutOfBoundsException)
    {
        null
    }
}

object ChatService
{
    private val chatList: MutableList<Chat> = mutableListOf()
    private var chatId: Long = 0
    private var messageId: Long = 0

    fun message(from: Int, to: Int, message: String) {
        val foundChat = this.chatList.fromTo<Chat>(from, to)
        if (foundChat !== null)
        {
            println("Adding message to chat with ID: ${foundChat.chatId}")
            foundChat.messageList.plusAssign(ChatMessage(++this.messageId, from, message))
        } else {
            this.chatList+=Chat(++this.chatId, setOf(from, to), mutableListOf(ChatMessage(++this.messageId, from, message)))
            println("Created new chat with ID: ${this.chatId}")
        }
    }

    fun getChatList(): List<Chat> = this.chatList

    fun getMessages(chatId: Long, fromMessageId: Long? = null, limit: Int = -1): List<ChatMessage>?
    {
        val messages = this.getMessagesFromChat(chatId)
        if (messages.size > 0)
        {
            val outMessages = mutableListOf<ChatMessage>()
            if (fromMessageId != null)
                messages.filter { it.messageId >= fromMessageId }
            val chatSize = messages.size
            val endPos = if (limit < 0) { chatSize } else { if (chatSize > limit) { limit } else { chatSize } }
            outMessages+=messages.sorted().reversed().subList(0, endPos) // invert sorting to get latest messages
            outMessages.map { it.isRead = true }
            return outMessages
        // last unreaded $limit messages
        }
        return null
    }

    fun deleteChat(chatId: Long)
    {
        this.chatList-=this.chatList.filter { it.chatId == chatId }
    }

    fun deleteMessage(chatId: Long, messageId: Long)
    {
        val chat = this.chatList.byId<ChatMessage>(chatId)
        if (chat != null) {
            chat-=chat.filter { it.messageId == messageId }
        }
    }

    fun getMessagesFromChat(chatId: Long): MutableList<ChatMessage>
    {
        return this.chatList.byId<ChatMessage>(chatId) ?: mutableListOf()
    }

}
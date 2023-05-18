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

fun <E> Iterable<Chat>.byId(chatId: Long): List<ChatMessage>? {
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

    fun getLastMessagesFromChat(chatId: Long, limit: Int = 5): List<ChatMessage>?
    {
        val messages = this.getMessagesFromChat(chatId)
        if (messages !== null)
        {
            val chatSize = messages.size
            val startPos = if (chatSize >= limit) { chatSize-limit } else { 0 }
            return messages.sorted().subList(startPos, chatSize) // last $limit messages
        }
        return null
    }


/*    fun getMessages(from: Int, to: Int, onlyNew: Boolean = false): List<ChatMessage>
    {
        val fromTo = fun(message: ChatMessage) = (message.from == from && message.to == to) || (message.from == to && message.to == from)
        val messageList = this.messageList.filter(fromTo)
        if (onlyNew)
           messageList.filter(fun(message: ChatMessage) = !message.isRead)
        return messageList
    }*/
    fun getChats(to: Int)
    {

    }

    fun getMessagesFromChat(chatId: Long): List<ChatMessage>?
    {
        return this.chatList.byId<ChatMessage>(chatId)
    }

}
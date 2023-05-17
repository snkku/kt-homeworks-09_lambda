package ru.netology

data class Chat(
    val chatId: Long,
    val recipients: Set<Int> = mutableSetOf(),
    val messageList: MutableList<ChatMessage>
)

data class ChatMessage(
    val messageId: Long,
    val from: Int,
    val text: String,
    val isRead: Boolean = false,
    val isDeleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

/*
data class chatPair(
    val firstUserId: Int,
    val secondUserId: Int,
    val messageList: MutableList<chatMessage> = mutableListOf<chatMessage>()
)
{
    fun message(from: Int, to: Int, message: String)
    {
        this.messageList.plus(chatMessage(from, to, message))
    }

}
*/

object ChatService
{
    private val chatList: MutableList<Chat> = mutableListOf()
    private var chatId: Long = 0
    private var messageId: Long = 0

    fun message(from: Int, to: Int, message: String) {
        var foundChat = this.chatList.filter(fun(chat: Chat) = chat.recipients.containsAll(listOf(from, to)))
        if (foundChat.isEmpty())
        {
            println("Chat not found! Creating new")
            this.chatList+=Chat(++chatId, setOf(from, to), mutableListOf(ChatMessage(++messageId, from, message)))
        } else {
            println("Chat already exists")
            foundChat.plus(ChatMessage(++messageId, from, message))
            println(foundChat)
        }
        //this.messageList += ChatMessage(++this.messageId, from, to, message)
    }

/*
    fun getMessages(from: Int, to: Int, onlyNew: Boolean = false): List<ChatMessage>
    {
        val fromTo = fun(message: ChatMessage) = (message.from == from && message.to == to) || (message.from == to && message.to == from)
        val messageList = this.messageList.filter(fromTo)
        if (onlyNew)
           messageList.filter(fun(message: ChatMessage) = !message.isRead)
        return messageList
    }
    fun getChats(to: Int)
    {

    }
*/

}
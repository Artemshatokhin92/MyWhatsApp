package com.shatokhin.mywhatsapp.domain.repository

import com.shatokhin.mywhatsapp.domain.models.ChatData
import com.shatokhin.mywhatsapp.domain.models.Message

interface RepositoryChatData {
    fun getPageChats(page: Int): List<ChatData>
    fun refreshListChats()
    fun getCountPageChatData(): Int
    fun getNextPageListMessage(idOldestMessage: String, page: Int): List<Message>
    fun getListMessageRangeUpToMessage(nameChat: String, countMessage: Int): List<Message>
    fun getCountPageMessageFromServer(chatName: String): Int
    fun editMessagesInChatData(nameChat: String)
    fun getOnePageListMessage(nameChat: String): List<Message>
}
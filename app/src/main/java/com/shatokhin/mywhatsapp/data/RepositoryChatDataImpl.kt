package com.shatokhin.mywhatsapp.data

import com.shatokhin.mywhatsapp.domain.models.ChatData
import com.shatokhin.mywhatsapp.domain.models.Message
import com.shatokhin.mywhatsapp.domain.repository.RepositoryChatData


class RepositoryChatDataImpl: RepositoryChatData {

    companion object{
        private var instance: RepositoryChatDataImpl? = null

        fun getInstance(): RepositoryChatDataImpl? {
            if (instance == null) {
                instance = RepositoryChatDataImpl()
            }
            return instance
        }
    }


    private val server = Server()

    // 1.2.1. В классе Repository реализовать функцию getChats(): List<ChatData>
    override fun getPageChats(page: Int): List<ChatData> {
        return server.getPageChats(page)
    }

    override fun refreshListChats() {
        server.refreshListChats()
    }

    override fun getCountPageChatData(): Int {
        return server.getCountPageChatData()
    }

    override fun getNextPageListMessage(idOldestMessage: String, page: Int): List<Message> {
        return server.getNextPageListMessage(idOldestMessage, page)
    }

    override fun getListMessageRangeUpToMessage(nameChat: String, idMessage: Int): List<Message> {
        return server.getListMessageRangeUpToMessage(nameChat, idMessage)
    }

    override fun getCountPageMessageFromServer(chatName: String): Int {
        return server.getCountPageMessageFromServer(chatName)
    }

    override fun editMessagesInChatData(nameChat: String) {
        server.editMessagesInChatData(nameChat)
    }

    override fun getOnePageListMessage(nameChat: String): List<Message> {
        return server.getOnePageListMessage(nameChat)
    }

}
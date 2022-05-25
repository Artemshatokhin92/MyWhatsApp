package com.shatokhin.mywhatsapp.data

import android.util.Log
import com.shatokhin.mywhatsapp.domain.models.ChatData
import com.shatokhin.mywhatsapp.domain.models.Message
import com.shatokhin.mywhatsapp.utilites.TAG_FOR_LOGCAT

class Server {
    private val listChatData = mutableListOf<ChatData>()
    private val sizePageChats = 15 // объем страницы
    private val sizePageMessage = 20 // объем страницы

    // 1.2.3 Список чатов хранящийся на сервере периодически меняется
    fun refreshListChats() {
        listChatData.clear()
        val countChats = (30..90).random()
        for (i in 0..countChats){
            listChatData.add(getRandomChatData()) // 1.2.4. Содержимое данных в объекте чата (ChatData) так же периодически меняются
        }
        Log.d(TAG_FOR_LOGCAT, "Сгенерирован новый список длиной ${listChatData.size}")
    }

    fun getPageChats(page: Int): List<ChatData> {
        val listChat = mutableListOf<ChatData>()

        val startIndex = page * sizePageChats - 10 // начало страницы
        var endIndex = startIndex + sizePageChats - 1 // конец страницы

        if (endIndex > listChatData.size) { // если endIndex выходит за пределы массива, происходит когда запрашивается последняя страница (page)
            endIndex = listChatData.size - 1 // устанавливаем endIndex равному индексу последнего элемента в массиве, чтобы догрузить оставшиеся элементы
            if (endIndex <= startIndex) return arrayListOf() // в иных случаях, если последняя страница уже передана
        }

        for (i in startIndex..endIndex){
            listChat.add(listChatData[i])
        }
        return listChat
    }

    fun getCountPageChatData(): Int {
        val result = listChatData.size / sizePageChats // получаем количество полных страниц
        if (listChatData.size % sizePageChats != 0) return result + 1 // если есть остаток, значит есть еще одна, но уже не полная страница (поэтому +1)
        return result
    }


    // 1.2.4. Содержимое данных в объекте чата (ChatData) так же периодически меняются, это следует реализовать при моделировании getChats()
    private fun getRandomChatData(): ChatData {
        val nameChat = getRandomString( (3..10).random() )
        val listMessage = getRandomListMessage()
        return ChatData(nameChat, listMessage.toMutableList())
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = 'а'..'я'
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun getRandomTextMessage(): String {
        val result = StringBuffer()

        val countWords = (1..15).random()
        for (i in 0..countWords){
            val randomWorld = getRandomString( (3..7).random() )
            result.append(randomWorld)
            result.append(" ")
        }

        return result.toString()
    }

    private fun getRandomBoolean(): Boolean {
        return when((0..1).random()){
            0 -> false
            else -> true
        }
    }

    private fun getRandomMessage(id: Int): Message {
        return Message(id, getRandomTextMessage(), true, "17:49", getRandomBoolean())
    }

    private fun getRandomListMessage(): List<Message> {
        val countMessage = (5..90).random()
        val listMessage = mutableListOf<Message>()
        for (i in 0..countMessage){
            listMessage.add(getRandomMessage(i))
        }
        return listMessage
    }

    fun getNextPageListMessage(nameChat: String, idOldestMessage: Int): List<Message> {
        var indexChatData = 0
        listChatData.forEachIndexed { index, chatData ->
            if (chatData.name == nameChat) { indexChatData = index }
        }
        val listMessageCurrentChatData = listChatData[indexChatData].listMessage
        val result = mutableListOf<Message>()

        var indexOldestMessage = 0
        listMessageCurrentChatData.forEachIndexed { index, message ->
            if (message.id == idOldestMessage) indexOldestMessage = index
        }

        val startIndex = indexOldestMessage // начало страницы c конца
        var endIndex = startIndex - sizePageMessage + 1 // конец страницы

        if (endIndex < 0) { // если endIndex выходит за пределы массива, происходит когда запрашивается последняя страница (page)
            endIndex = 0 // устанавливаем endIndex равному индексу последнего элемента в массиве, чтобы догрузить оставшиеся элементы
            if (endIndex == startIndex) return arrayListOf() // в иных случаях, если последняя страница уже передана
        }

        for (i in startIndex downTo endIndex){
            result.add(listMessageCurrentChatData[i])
        }

        return result
    }

    fun getListMessageRangeUpToMessage(nameChat: String, idMessage: Int): List<Message> {
        var indexChatData = 0
        listChatData.forEachIndexed { index, chatData ->
            if (chatData.name == nameChat) indexChatData = index
        }
        val listMessageCurrentChatData = listChatData[indexChatData].listMessage

        val result = mutableListOf<Message>()

        val startIndex = listMessageCurrentChatData.size - 1 // начало страницы c конца
        var endIndex = 0 // конец страницы

        listMessageCurrentChatData.forEachIndexed { index, message ->
            if (message.id == idMessage) endIndex = index
        }

        for (i in  startIndex downTo endIndex){
            result.add(listMessageCurrentChatData[i])
        }

        return result
    }

    fun getCountPageMessageFromServer(chatName: String): Int {
        var countMessage = 0
        listChatData.forEach {
            if (it.name == chatName) countMessage = it.listMessage.size
        }
        if (countMessage == 0) return 0
        val result = countMessage / sizePageMessage
        if (countMessage % sizePageMessage != 0) return result + 1 // если есть остаток, значит есть еще одна, но уже не полная страница (поэтому +1)
        return result
    }

    fun editMessagesInChatData(nameChat: String) {
        var indexChatData = 0
        listChatData.forEachIndexed { index, chatData ->
            if (chatData.name == nameChat) indexChatData = index
        }
        for (i in 0..3) editTextRandomMessage(indexChatData)
        for (i in 0..1) deleteTextRandomMessage(indexChatData)
        for (i in 0..1) addNewMessageInterlocutor(indexChatData)
    }

    private fun editTextRandomMessage(indexChatData: Int) {
        val listMessage = listChatData[indexChatData].listMessage

        val randomIndexMessage = getRandomIndexMessageInterlocutor(listMessage)
        val message = listMessage[randomIndexMessage]

        val editedMessage = Message(message.id, message.text + " (текст был изменен)", message.wasViewed, message.dateReceipt, message.isInterlocutor)
        listMessage[randomIndexMessage] = editedMessage
    }

    private fun deleteTextRandomMessage(indexChatData: Int) {
        val listMessage = listChatData[indexChatData].listMessage

        val randomIndexMessage = getRandomIndexMessageInterlocutor(listMessage)
        val message = listMessage[randomIndexMessage]

        val editedMessage = Message(message.id, "Сообщение удалено собеседником", message.wasViewed, message.dateReceipt, message.isInterlocutor)
        listMessage[randomIndexMessage] = editedMessage
    }

    private fun addNewMessageInterlocutor(indexChatData: Int){
        val listMessage = listChatData[indexChatData].listMessage

        listMessage.add( Message(listMessage.size, getRandomTextMessage(), true, "17:49", true) )
    }

    private fun getRandomIndexMessageInterlocutor(listMessage: MutableList<Message>): Int{
        var randomIndexMessage = (0 until listMessage.size).random()

        while (!listMessage[randomIndexMessage].isInterlocutor) randomIndexMessage = (0 until listMessage.size).random()

        return randomIndexMessage

    }

    fun addMessage(indexChatData: Int){
        val listMessage = listChatData[indexChatData].listMessage
    }
    fun getOnePageListMessage(nameChat: String): List<Message> {
        var indexChatData = 0
        listChatData.forEachIndexed { index, chatData ->
            if (chatData.name == nameChat) indexChatData = index
        }
        val listMessageCurrentChatData = listChatData[indexChatData].listMessage

        val result = mutableListOf<Message>()

        val startIndex = listMessageCurrentChatData.size - 1 // начало страницы c конца
        var endIndex = startIndex - sizePageMessage + 1 // конец страницы

        if (endIndex < 0) { // если endIndex выходит за пределы массива, происходит когда запрашивается последняя страница (page)
            endIndex = 0 // устанавливаем endIndex равному индексу последнего элемента в массиве, чтобы догрузить оставшиеся элементы
            if (endIndex == startIndex || startIndex < 0) return arrayListOf() // в иных случаях, если последняя страница уже передана
        }

        for (i in startIndex downTo endIndex){
            result.add(listMessageCurrentChatData[i])
        }

        return result
    }


}
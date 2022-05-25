package com.shatokhin.mywhatsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shatokhin.mywhatsapp.domain.models.Message
import com.shatokhin.mywhatsapp.domain.repository.RepositoryChatData

class ViewModelChat(private val repository: RepositoryChatData): ViewModel() {

    private val mListMessageCurrentChatData = MutableLiveData<List<Message>>()
    val listMessageCurrentChatData: LiveData<List<Message>>
        get() = mListMessageCurrentChatData

    var currentChatName: String = ""
        set(value) {
            mListMessageCurrentChatData.value = listOf()
            field = value
            getOnePageListMessage()
        }

    fun getNextPageListMessage(idOldestMessage: Int) {
        var newList = mListMessageCurrentChatData.value?.toMutableList()
        if (newList == null) newList = mutableListOf()
        repository.getNextPageListMessage(currentChatName, idOldestMessage).forEach { message ->
            newList.add(message)
        }
        mListMessageCurrentChatData.value = newList.toList()
    }

    fun getListMessageRangeUpToMessage(idOldestMessage: Int) {
        val newList: MutableList<Message> = mutableListOf()
        repository.getListMessageRangeUpToMessage(currentChatName, idOldestMessage).forEach { message ->
            newList.add(message)
        }
        mListMessageCurrentChatData.value = newList.toList()
    }

    fun editMessagesInChatDataInServer() {
        repository.editMessagesInChatData(currentChatName)
    }

    private fun getOnePageListMessage() {
        var newList = mListMessageCurrentChatData.value?.toMutableList()
        if (newList == null) newList = mutableListOf()
        repository.getOnePageListMessage(currentChatName).forEach { message ->
            newList.add(message)
        }
        mListMessageCurrentChatData.value = newList.toList()
    }
}
package com.shatokhin.mywhatsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shatokhin.mywhatsapp.domain.models.ChatData
import com.shatokhin.mywhatsapp.domain.repository.RepositoryChatData

class ViewModelListChats(private val repository: RepositoryChatData ): ViewModel() {

    private val mListChatData = MutableLiveData<List<ChatData>>()
    val listChatData: LiveData<List<ChatData>>
        get() = mListChatData


    fun getPageChats(page: Int){
        var newList = mListChatData.value?.toMutableList()

        if (newList == null) newList = mutableListOf()

        repository.getPageChats(page).forEach { chatData ->
            newList.add(chatData)
        }
        mListChatData.value = newList.toList()

    }

    fun getCountPageChatsFromServer(): Int {
        return repository.getCountPageChatData()
    }

    fun refreshListChats(){
        mListChatData.value = listOf()
        repository.refreshListChats()
    }


}
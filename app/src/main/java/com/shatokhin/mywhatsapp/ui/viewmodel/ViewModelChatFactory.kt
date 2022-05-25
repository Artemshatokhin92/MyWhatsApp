package com.shatokhin.mywhatsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shatokhin.mywhatsapp.data.RepositoryChatDataImpl

class ViewModelChatFactory: ViewModelProvider.Factory {

    private val repositoryChatData by lazy (LazyThreadSafetyMode.NONE) { RepositoryChatDataImpl.getInstance() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelChat(repositoryChatData!!) as T
    }

}
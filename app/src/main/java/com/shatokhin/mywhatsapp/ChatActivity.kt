package com.shatokhin.mywhatsapp

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shatokhin.mywhatsapp.databinding.ActivityChatBinding
import com.shatokhin.mywhatsapp.databinding.ViewHolderMessageBinding
import com.shatokhin.mywhatsapp.domain.models.Message
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelChat
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelChatFactory
import com.shatokhin.mywhatsapp.utilites.EXTRA_NAME_OPEN_CHAT
import com.shatokhin.mywhatsapp.utilites.TAG_FOR_LOGCAT


class ChatActivity : AppCompatActivity() {
    private val viewModelChat: ViewModelChat by viewModels { ViewModelChatFactory() }
    private var listMessage: List<Message> = mutableListOf()
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nameChat = intent.getStringExtra(EXTRA_NAME_OPEN_CHAT)
        nameChat?.let {
            viewModelChat.currentChatName = it
        }

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelChat.listMessageCurrentChatData.observe(this){ newListMessage ->
            setListMessage(newListMessage)
        }

        binding.scrollMessage.viewTreeObserver.addOnScrollChangedListener {
            if ( isViewVisible(getTriggerMessage()) ) {
                loadNextPageListMessage()
            }
        }
        binding.swiperefreshChat.setOnRefreshListener {
            viewModelChat.editMessagesInChatDataInServer()
            val idOldestMessage = listMessage[listMessage.size - 1].id
            viewModelChat.getListMessageRangeUpToMessage(idOldestMessage)
            binding.swiperefreshChat.isRefreshing = false
        }

    }

    private fun loadNextPageListMessage(){
        val idOldestMessage = listMessage[listMessage.size - 1].id
        if (idOldestMessage != 0) viewModelChat.getNextPageListMessage(idOldestMessage)
    }

    private fun getTriggerMessage(): View {
        val indexLastMessage = binding.containerForMessage.childCount - 1
        return binding.containerForMessage.getChildAt(indexLastMessage - 1)
    }

    private fun isViewVisible(view: View): Boolean {
        val scrollBounds = Rect();
        binding.scrollMessage.getDrawingRect(scrollBounds);

        val top = view.y;
        val bottom = top + view.height

        return scrollBounds.bottom > bottom
    }

    private fun setListMessage(newListMessage: List<Message>){
        // если сообщений нет, то добавляем без проверки
        if (listMessage.isEmpty()) {
            listMessage = newListMessage
            newListMessage.forEach {
                addInEndViewHolderMessageInContainerForMessage(it)
            }
        }
        // если сообщения уже имеются, то определяем куда добавлять новые сообщения или какие старые сообщения изменить
        else{
            refreshContainerForMessage(newListMessage)
        }
        Log.d(TAG_FOR_LOGCAT, "ChatActivity setListMessage: ${listMessage.size}")
    }

    private fun refreshContainerForMessage(newListMessage: List<Message>){
        val countMessageInContainer = binding.containerForMessage.childCount
//        val listNewMessage = mutableListOf<Message>()

        val listMessageForAddStart = mutableListOf<Message>()
        val listMessageForEditing = mutableListOf<Message>()
        val listMessageForAddEnd = mutableListOf<Message>()

        val sizeList = listMessage.size
        val idNewestMessage = listMessage[0].id
        val idOldestMessage = listMessage[sizeList-1].id

        newListMessage.forEach {
            when{
                it.id > idNewestMessage -> listMessageForAddStart.add(it)
                it.id <= idNewestMessage && it.id >= idOldestMessage -> listMessageForEditing.add(it)
                it.id < idOldestMessage -> listMessageForAddEnd.add(it)
            }
        }

        for (i in 0 until countMessageInContainer) editViewHolderMessage(listMessageForEditing[i], i)

        for (i in listMessageForAddStart.size - 1 downTo 0) addInStartViewHolderMessageInContainerForMessage(listMessageForAddStart[i])

        for (i in 0 until listMessageForAddEnd.size) addInEndViewHolderMessageInContainerForMessage(listMessageForAddEnd[i])

        listMessage = newListMessage
    }

    private fun editViewHolderMessage(message: Message, position: Int){

        val viewHolderMessage = binding.containerForMessage.getChildAt(position)

        val tvTextMessage = viewHolderMessage.findViewById<TextView>(R.id.tvTextMessage)
        tvTextMessage.text = message.text

    }

    private fun addInStartViewHolderMessageInContainerForMessage(message: Message){

        val bindingViewHolderMessage = if(message.isInterlocutor) getViewHolderInterlocutorMessage()
        else getViewHolderMyMessage()

        bindingViewHolderMessage.message = message
        binding.containerForMessage.addView(bindingViewHolderMessage.root, 0)
    }

    private fun addInEndViewHolderMessageInContainerForMessage(message: Message){

        val bindingViewHolderMessage = if(message.isInterlocutor) getViewHolderInterlocutorMessage()
            else getViewHolderMyMessage()

        bindingViewHolderMessage.message = message
        binding.containerForMessage.addView(bindingViewHolderMessage.root)
    }

    private fun getViewHolderMyMessage(): ViewHolderMessageBinding {
        val viewHolder = ViewHolderMessageBinding.inflate(layoutInflater)
        viewHolder.containerMain.gravity = Gravity.END
        viewHolder.containerMessage.background.setTint(ContextCompat.getColor(this, R.color.color_my_message))
        return viewHolder
    }

    private fun getViewHolderInterlocutorMessage(): ViewHolderMessageBinding {
        val viewHolder = ViewHolderMessageBinding.inflate(layoutInflater)
        viewHolder.containerMain.gravity = Gravity.START
        viewHolder.containerMessage.background.setTint(ContextCompat.getColor(this, R.color.color_interlocutor_message))
        return viewHolder
    }

}
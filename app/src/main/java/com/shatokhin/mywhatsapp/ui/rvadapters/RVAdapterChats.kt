package com.shatokhin.mywhatsapp.ui.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shatokhin.mywhatsapp.databinding.ViewHolderChatsBinding
import com.shatokhin.mywhatsapp.domain.models.ChatData

class RVAdapterChats (private val clickListenerChatData: ClickListenerChatData)
    : ListAdapter<ChatData, RVAdapterChats.VHChats>(ChatsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHChats {
        val viewHolderChatsBinding = ViewHolderChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHChats(viewHolderChatsBinding, clickListenerChatData)
    }

    override fun onBindViewHolder(holderChats: VHChats, position: Int) {
        val chatData = getItem(position)
        holderChats.bind(chatData)
    }

    fun getSizeListChats(): Int = itemCount

    class VHChats(private val binding: ViewHolderChatsBinding, private val clickListenerChatData: ClickListenerChatData)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatData: ChatData){
            binding.chatData = chatData
            binding.root.setOnClickListener {
                clickListenerChatData.openChat(chatData)
            }
        }
    }

    interface ClickListenerChatData{
        fun openChat(chatData: ChatData)
    }
}


object ChatsDiffCallback : DiffUtil.ItemCallback<ChatData>() {
    override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
        return oldItem == newItem
    }
}


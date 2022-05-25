package com.shatokhin.mywhatsapp.domain.models

data class ChatData(val name: String, var listMessage: MutableList<Message>){

    fun getLastMessage(): Message = listMessage[listMessage.size - 1]

}



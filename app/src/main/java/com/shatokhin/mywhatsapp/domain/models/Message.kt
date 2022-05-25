package com.shatokhin.mywhatsapp.domain.models

class Message(val id: Int, val text: String, var wasViewed: Boolean, val dateReceipt: String, val isInterlocutor: Boolean) {
    fun getIdString() = id.toString()
}
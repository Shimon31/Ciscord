package com.example.shimon.ciscord


interface Message {
    val senderId: String
    val receiverId: String
    var msgId: String
}


data class TextMessage(
    var text: String? = null,
    override val senderId: String = "",
    override val receiverId: String = "",
    override var msgId: String = ""
) : Message


data class ImageMessage(

    val imageLink: String = "",
    override val senderId: String = "",
    override val receiverId: String = "",
    override var msgId: String = "",
) : Message


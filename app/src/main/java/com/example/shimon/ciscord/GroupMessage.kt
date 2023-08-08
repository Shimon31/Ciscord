package com.example.shimon.ciscord


data class GroupTextMessage(
    var text: String? = null,
    val senderId: String = "",
    val receiverId: List<String> = emptyList(),
    var msgId: String = ""
)

package com.example.shimon.ciscord

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group (
    val groupName : String = "",
    val groupImage : String = "",
    val groupBio : String = "",
    val groupAdmin : String = "",
    val groupIdr : String = "",
    val groupMember : MutableList<String> = mutableListOf(),

    ): Parcelable


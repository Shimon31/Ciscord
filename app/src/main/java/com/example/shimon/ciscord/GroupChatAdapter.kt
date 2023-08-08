package com.example.shimon.ciscord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GroupChatAdapter(var userIdSelf: String, private val chatList: MutableList<GroupTextMessage>) :
RecyclerView.Adapter<GroupChatAdapter.ChatViewHolder>() {

    private val Left: Int = 1
    private val Right: Int = 2



    class ChatViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTv: TextView = itemView.findViewById(R.id.chatTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return if (viewType == Right) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.right_chat_item, parent, false)
            ChatViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.left_chat_item, parent, false)
            ChatViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
       val message = chatList[position]

        holder.messageTv.text = message.text


    }

    override fun getItemViewType(position: Int): Int {

        return if (chatList[position].senderId == userIdSelf) {

            Right
        } else {

            Left

        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }


}
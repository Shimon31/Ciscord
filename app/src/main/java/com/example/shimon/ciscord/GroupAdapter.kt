package com.example.shimon.ciscord

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shimon.ciscord.databinding.FragmentGroupsBinding
import com.example.shimon.ciscord.databinding.ItemGroupsBinding

class GroupAdapter(
    var groupList: MutableList<Group>,
    var currentUserID: String,
    var listener: GroupAdapter.Listener
) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    interface Listener {

        fun joinGroup(userID: String, groupId: String, groupMember: MutableList<String>)
        fun groupChat(group: Group)


    }


    class GroupViewHolder(var binding: ItemGroupsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(
            ItemGroupsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]

        if (!currentUserID.containsUser(group.groupMember)) {

            holder.binding.joinGroupTV.text = Join

        } else {

            holder.binding.joinGroupTV.text = Chat
        }


        holder.binding.joinGroupTV.setOnClickListener {

            if (holder.binding.joinGroupTV.text.toString() == Join) {
                listener.joinGroup(
                    currentUserID,
                    groupId = group.groupIdr,
                    groupMember = group.groupMember
                )
            } else {

                listener.groupChat(group)
            }

        }


        holder.binding.GroupName.text = group.groupName
        holder.binding.groupBio.text = group.groupBio
        holder.binding.profileImage.load(group.groupImage)


    }

    companion object {

        const val Join = "Join Group"
        const val Chat = "Join Chat"

    }

    private fun String.containsUser(keywords: MutableList<String>): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword, true)) return true
        }
        return false
    }
}
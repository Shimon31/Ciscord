package com.example.shimon.ciscord

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shimon.ciscord.databinding.ItemUserBinding

class UserAdapter(val userListener: UserListener) :
    androidx.recyclerview.widget.ListAdapter<User, UserAdapter.UserViewHolder>(COMPARATOR) {

    interface UserListener {

        fun userItemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let {
            holder.binding.apply {

                Log.d("userAdapter","$it")

                fullName.text = it.fullName
                userBio.text = it.bio
                email.text = it.email

                holder.itemView.setOnClickListener { _ ->
                    userListener.userItemClick(it)
                }
            }
        }
    }

    companion object{
        val COMPARATOR = object : DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }


        }

    }


    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)


}
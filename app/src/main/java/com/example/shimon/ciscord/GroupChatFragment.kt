package com.example.shimon.ciscord

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.shimon.ciscord.databinding.FragmentGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class GroupChatFragment : Fragment() {
    lateinit var binding: FragmentGroupChatBinding
    private lateinit var group: Group
    lateinit var chatDB: DatabaseReference
    lateinit var userSelfId: String
    lateinit var userSelfIdRemote: String

    val chatList: MutableList<GroupTextMessage> = emptyList<GroupTextMessage>().toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupChatBinding.inflate(layoutInflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.chatRCV.layoutManager = layoutManager


        group = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            requireArguments().getParcelable<Group>("key", Group::class.java)!!
        } else {
            requireArguments().getParcelable<Group>("key")!!
        }

        Log.d("group", "$group")

        chatDB = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userSelfId = it.uid
        }

        chatDB.child(DBNOTES.GROUP).child(group.groupIdr)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(Group::class.java)?.let {

                        Glide.with(requireContext()).load(it.groupImage)
                            .placeholder(R.drawable.placeholder).into(binding.profilePicture)

                        binding.profileName.text = it.groupName

                        /*   binding.gmailTV.text = it.*/


                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })


        allMessageShow()

        return binding.root
    }

    private fun allMessageShow() {
        chatDB.child(DBNOTES.GROUP_CHATS).child(group.groupIdr)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    chatList.clear()

                    snapshot.children.forEach { snap ->
                        snap.getValue(GroupTextMessage::class.java)?.let {

                            chatList.add(it)


                        }

                    }

                    val adapter = GroupChatAdapter(userSelfId, chatList)

                    binding.chatRCV.adapter = adapter


                }

                override fun onCancelled(error: DatabaseError) {


                }

            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendBTN.setOnClickListener {

            var textMessage = GroupTextMessage(
                text = binding.chatET.text.toString(),
                senderId = userSelfId,
                receiverId = group.groupMember,
                msgId = ""
            )

            sendTextMessage(textMessage)

        }

    }

    private fun sendTextMessage(textMessage: GroupTextMessage) {
        val msgId = chatDB.push().key ?: UUID.randomUUID().toString()
        textMessage.msgId = msgId

        chatDB.child(DBNOTES.GROUP_CHATS).child(group.groupIdr).child(msgId).setValue(textMessage)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Sent Successfully", Toast.LENGTH_SHORT).show()
                    binding.chatET.setText("")
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${it.exception?.message} : Something went wrong ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


    }


}
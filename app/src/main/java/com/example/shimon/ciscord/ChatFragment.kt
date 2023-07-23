package com.example.shimon.ciscord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.example.shimon.ciscord.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatDB: DatabaseReference
    lateinit var userSelfId: String
    lateinit var userSelfIdRemote: String
    val chatList = mutableListOf<TextMessage>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.chatRCV.layoutManager = layoutManager


        requireArguments().getString(USERID)?.let {
            userSelfIdRemote = it
        }


        chatDB = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userSelfId = it.uid
        }

        chatDB.child(DBNOTES.USER).child(userSelfIdRemote)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.getValue(User::class.java)?.let {

                        Glide.with(requireContext()).load(it.profileImage)
                            .placeholder(R.drawable.placeholder).into(binding.profilePicture)
                        binding.profileName.text = it.fullName
                        binding.gmailTV.text = it.email


                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

        allMessageShow()



        return binding.root
    }

    private fun allMessageShow() {
        chatDB.child(DBNOTES.CHAT).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()

                snapshot.children.forEach { snap ->
                    snap.getValue(TextMessage::class.java)?.let {
                        if (it.senderId == userSelfId && it.receiverId == userSelfIdRemote ||
                            it.senderId == userSelfIdRemote && it.receiverId == userSelfId){

                            chatList.add(it)

                        }

                    }

                }

                val adapter = ChatAdapter(userSelfId,chatList)

                binding.chatRCV.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {


            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendBTN.setOnClickListener {

            var textMessage = TextMessage(
                text = binding.chatET.text.toString(),
                senderId = userSelfId,
                receiverId = userSelfIdRemote,
                msgId = ""
            )

            sendTextMessage(textMessage)

        }
    }

    private fun sendTextMessage(textMessage: TextMessage) {
        val msgId = chatDB.push().key ?: UUID.randomUUID().toString()
        textMessage.msgId = msgId

        chatDB.child(DBNOTES.CHAT).child(msgId).setValue(textMessage).addOnCompleteListener {

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

    companion object {
        const val USERID = "user_id_key"
    }


}

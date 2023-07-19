package com.example.shimon.ciscord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shimon.ciscord.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatDB: DatabaseReference
    lateinit var userSelfId: String
    lateinit var userSelfIdRemote: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        requireArguments().getString(USERID)?.let {
            userSelfIdRemote = it
        }


        chatDB = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userSelfId = it.uid
        }

        return binding.root
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
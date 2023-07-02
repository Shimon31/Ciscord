package com.example.shimon.ciscord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.shimon.ciscord.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    lateinit var userDB: DatabaseReference

    private var userId = ""

    private var bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        userDB = FirebaseDatabase.getInstance().reference


        requireArguments().getString(USERID)?.let {

            userId = it

            Log.d("profileFragment", "before get user by getUserByID: $it")
            getUserByID(it)

        }

        FirebaseAuth.getInstance().currentUser?.let {
            if (userId == it.uid) {

                binding.chatWithUserBTn.text = edit
            } else {
                binding.chatWithUserBTn.text = chat
            }

        }

        binding.chatWithUserBTn.setOnClickListener {

            if (binding.chatWithUserBTn.text == edit) {

                bundle.putString(editProfileFragment.USERID, userId)
                findNavController().navigate(
                    R.id.action_profileFragment_to_editProfileFragment,
                    bundle
                )
            }

        }





        return binding.root
    }


    companion object {
        const val USERID = "user_id_key"
        const val edit = "Edit Profile"
        const val chat = "Lets chat"
    }

    private fun getUserByID(userId: String) {

        Log.d("user", "$userId")

        userDB.child(DBNOTES.USER).child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.getValue(User::class.java)?.let {

                    binding.userEmail.text = it.email
                    binding.bio.text = it.bio
                    binding.fullName.text = it.fullName


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }


}
package com.example.shimon.ciscord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shimon.ciscord.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class editProfileFragment : Fragment() {



    lateinit var binding:FragmentEditProfileBinding

    lateinit var userDB: DatabaseReference

    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)


        userDB = FirebaseDatabase.getInstance().reference


        requireArguments().getString(USERID)?.let {

            userId = it

            getUserByID(it)

        }


        binding.saveBTN.setOnClickListener {

            val userMap:MutableMap<String, Any> = mutableMapOf()

            userMap["fullName"] = binding.editName.text.toString().trim()
            userMap["bio"] = binding.editBio.text.toString().trim()

            userDB.child(DBNOTES.USER).child(userId).updateChildren(userMap).addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Profile Updated ! ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }

        return binding.root
    }

    companion object {
        const val USERID = "user_id_key"
    }

    private fun getUserByID(userId: String) {



        userDB.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.getValue(User::class.java)?.let {

                    binding.userEmail.text = it.email
                    binding.editBio.setText(it.bio)
                    binding.editName.setText(it.fullName)


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }



}
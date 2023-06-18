package com.example.shimon.ciscord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shimon.ciscord.databinding.FragmentProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    lateinit var userDB: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        userDB = FirebaseDatabase.getInstance().getReference(DBNOTES.USER)
        requireArguments().getString(USERID)?.let {

            Log.d("profileFragment","before get user by getUserByID: $it")
            getUserByID(it)



        }



        return binding.root
    }


    companion object {
        const val USERID = "user_id_key"
    }

    private fun getUserByID(userId: String) {

        Log.d("user","$userId")

        userDB.child(userId).addValueEventListener(object : ValueEventListener {
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
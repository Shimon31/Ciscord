package com.example.shimon.ciscord

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shimon.ciscord.databinding.FragmentHomeBinding
import com.example.shimon.ciscord.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), UserAdapter.UserListener {
    lateinit var binding: FragmentHomeBinding
    lateinit var userDb: DatabaseReference
    lateinit var adapter: UserAdapter

    var userList: MutableList<User> = mutableListOf()

    private val auth = FirebaseAuth.getInstance()
    private lateinit var firebaseUSer: FirebaseUser

    private var currentUser: User? = null

    var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        userDb = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {

            firebaseUSer = it

        }

        binding.profileImage.setOnClickListener {

            currentUser?.let {
                bundle.putString(ProfileFragment.USERID, it.userId)
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment,bundle)

            }

        }



        binding.logoutBtn.setOnClickListener {


            auth.signOut().apply {

                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

            }

        }

        adapter = UserAdapter(this@HomeFragment)

        binding.recyclerView.adapter = adapter

        getAllAvailableUser()



        return binding.root
    }

    private fun getAllAvailableUser() {

        userDb.child(DBNOTES.USER).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                userList.clear()
                snapshot.children.forEach {

                    val user: User = it.getValue(User::class.java)!!

                    if (firebaseUSer.uid != user.userId) {
                        userList.add(user)
                    } else {
                        currentUser = user
                        setprofile()
                    }


                }
                adapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    private fun setprofile() {
        currentUser?.let {
            binding.profileImage.load("https://hips.hearstapps.com/hmg-prod/images/gettyimages-1061959920.jpg")
        }
    }

    override fun userItemClick(user: User) {

        Log.d("hf", "${user.userId}")

        bundle.putString(ProfileFragment.USERID, user.userId)

        findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)
    }


}





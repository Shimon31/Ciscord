package com.example.shimon.ciscord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shimon.ciscord.databinding.FragmentGroupsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GroupsFragment : Fragment(), GroupAdapter.Listener {

    lateinit var binding: FragmentGroupsBinding
    lateinit var userDB: DatabaseReference

    val groupList = mutableListOf<Group>()
    lateinit var currentUserID: String

    val groupMember = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(layoutInflater, container, false)

        FirebaseAuth.getInstance().currentUser?.let {
            currentUserID = it.uid
        }

        userDB = FirebaseDatabase.getInstance().reference


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllGroup()
    }

    private fun getAllGroup() {
        userDB.child(DBNOTES.GROUP).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                snapshot.children.forEach {
                    it.getValue(Group::class.java)?.let { group ->
                        groupList.add(group)
                    }

                }

                val adapter = GroupAdapter(
                    groupList = groupList,
                    currentUserID = currentUserID,
                    listener = this@GroupsFragment
                )
                binding.groupRCV.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    override fun joinGroup(userID: String, groupId: String, groupMember: MutableList<String>) {
        val members: MutableSet<String> = groupMember.toMutableSet()
        members.add(userID)
        var newGroupMember = members.toMutableList()
        val groupMap: MutableMap<String, Any> = mutableMapOf()
        groupMap["groupMember"] = newGroupMember

        userDB.child(DBNOTES.GROUP).child(groupId).updateChildren(groupMap).addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "User Added ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            getAllGroup()

        }

    }

    override fun groupChat(group: Group) {
        val bundle = Bundle()
        bundle.putParcelable("key",group)
        findNavController().navigate(R.id.action_groupsFragment_to_groupChatFragment,bundle)
    }

}
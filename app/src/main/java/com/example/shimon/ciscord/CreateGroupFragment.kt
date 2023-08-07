package com.example.shimon.ciscord

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.example.shimon.ciscord.databinding.FragmentCreateGroupBinding
import com.example.shimon.ciscord.databinding.FragmentEditProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class CreateGroupFragment : Fragment() {

    lateinit var binding: FragmentCreateGroupBinding

    lateinit var userDB: DatabaseReference
    lateinit var userStorage: StorageReference

    private var userId = ""
    private var imageLink: String = "no link"

    lateinit var currentUser: FirebaseUser
    private lateinit var userProfileUri: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateGroupBinding.inflate(inflater, container, false)

        FirebaseAuth.getInstance().currentUser?.let {

            userId = it.uid

        }
        userDB = FirebaseDatabase.getInstance().reference
        userStorage = FirebaseStorage.getInstance().reference



        binding.createGrpBTN.setOnClickListener {

            uploadImage(userProfileUri)

        }

        binding.groupImage.setOnClickListener {

            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }


        return binding.root
    }

    private fun uploadImage(userProfileUri: Uri) {
        var profileStorage: StorageReference =
            userStorage.child("Upload").child(userId).child("profile_Image")

        profileStorage.putFile(userProfileUri).addOnCompleteListener {
            if (it.isSuccessful) {
                profileStorage.downloadUrl.addOnSuccessListener { data ->

                    profileUpdateWithImage(data.toString())

                    Toast.makeText(
                        requireContext(),
                        "Image Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

    }

    private fun profileUpdateWithImage(imageLink: String) {

        val groupMember = mutableListOf<String>(userId)
        val groupId = userDB.push().key ?: UUID.randomUUID().toString()

        val userMap: MutableMap<String, Any> = mutableMapOf()

        userMap["groupName"] = binding.GRPName.text.toString().trim()
        userMap["groupImage"] = imageLink
        userMap["groupBio"] = binding.GRPBio.text.toString().trim()
        userMap["groupAdmin"] =userId
        userMap["groupMember"] =groupMember
        userMap["groupIdr"] = groupId


            userDB.child(DBNOTES.GROUP).child(groupId).updateChildren(userMap).addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Group Created", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }

    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    data?.data.let {
                        if (it != null) {
                            userProfileUri = it
                        }
                        binding.groupImage.setImageURI(it)
                    }


                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }


    private fun getUserByID(userId: String) {


        userDB.child(DBNOTES.USER).child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.getValue(User::class.java)?.let {

                    binding.GRPName.setText(it.fullName)
                    binding.GRPBio.setText(it.bio)
                    binding.groupImage.load(it.profileImage)


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }


}
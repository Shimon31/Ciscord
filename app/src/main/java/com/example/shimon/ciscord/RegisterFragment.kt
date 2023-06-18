package com.example.shimon.ciscord

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shimon.ciscord.databinding.FragmentLoginBinding
import com.example.shimon.ciscord.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    lateinit var userDb: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        userDb = FirebaseDatabase.getInstance().reference
        binding.buttonRegister.setOnClickListener {
            val name = binding.registerUsername.text.toString().trim()
            val email = binding.registerEmail.text.toString().trim()
            val password = binding.registerPassword.text.toString().trim()

            if (isEmailValid(email) && isPasswordValid(password)) {

                registerUser(name, email, password)
            } else {
                Toast.makeText(requireContext(), "Invalid Password and email", Toast.LENGTH_SHORT)
                    .show()
            }
        }






        return binding.root


    }

    private fun registerUser(name: String, email: String, password: String) {

        var auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->



            if (task.isSuccessful) {

                saveUserToDataBase(auth.currentUser?.uid, email, name)


            } else {

                Toast.makeText(
                    requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT
                ).show()

            }

        }

    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}$"
        return password.matches(passwordRegex.toRegex())
    }

    private fun saveUserToDataBase(uid: String?, email: String, name: String) {

        uid?.let {
            val user = User(
                userId = uid, email = email, fullName = name
            )


            userDb.child(DBNOTES.USER).child(it).setValue(user).addOnCompleteListener { task ->


                if (task.isSuccessful) {

                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                } else {

                    Toast.makeText(
                        requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT
                    ).show()


                }


            }


        }


    }


}
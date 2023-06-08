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
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.buttonLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (isEmailValid(email) && isPasswordValid(password)) {
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG)
                    .show()

            }


            loginUser(email, password)

        }



        return binding.root
    }

    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    Toast.makeText(
                        requireContext(),
                        "Login Successfully : ${user?.email}",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)

                } else {
                    // Login failed
                    Toast.makeText(
                        requireContext(),
                        "login Failed : ${task.exception?.message}",
                        Toast.LENGTH_SHORT
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


}
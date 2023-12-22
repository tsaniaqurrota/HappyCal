package com.example.uas_happycal.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.user.MainActivity
import com.example.uas_happycal.data.User
import com.example.uas_happycal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val email = binding.edtRegistEmail.text.toString()
            val password = binding.edtRegistPass.text.toString()
            val cpassword = binding.edtRegistConfirm.text.toString()
            val username = binding.edtRegistName.text.toString()
            val heightStr = binding.edtHeight.text.toString()
            val weightStr = binding.edtCurrentWeight.text.toString()
            val targetCalStr = binding.edtMaxCal.text.toString()

            val height = heightStr.toDoubleOrNull()
            val weight = weightStr.toDoubleOrNull()
            val targetCal = targetCalStr.toIntOrNull()

            val newUser = User(
                user_email = email,
                user_name = username,
                user_password = password,
                user_cpassword = cpassword,
                user_height = height ?: 0.0,
                user_weight = weight ?: 0.0,
                target_calories = targetCal ?: 0
            )

            val emailAvailable = Firebase.getUser(email)

            if (emailAvailable == null) {
                if (email.isNotEmpty() && password.isNotEmpty() && cpassword.isNotEmpty() &&
                    username.isNotEmpty() && height != null && weight != null && targetCal != null
                ) {
                    Firebase.addUser(newUser)
                    Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    val intentToMainActivity = Intent(context, MainActivity::class.java)
                    startActivity(intentToMainActivity)
                } else {
                    Toast.makeText(context, "Lengkapi semua kolom!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Email $email sudah terdaftar!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

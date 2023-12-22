package com.example.uas_happycal.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_happycal.auth.AuthActivity
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        prefManager = PrefManager.getInstance(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileUser = Firebase.getUser(prefManager.getEmail())

        with(binding) {

            if (profileUser != null) {
                Log.d("Profile Fragment", "User retrieved from Firebase: ${profileUser.user_name}")
                txtProfileEmail.text = profileUser?.user_email
                txtProfileName.text = profileUser?.user_name
                txtProfileTb.text = "${profileUser?.user_height.toString()} cm"
                txtProfileBb.text = "${profileUser?.user_weight.toString()} kg"
            } else {
                Log.d("HomeFragment", "User not found in Firebase. $txtProfileName")
            }

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                val intent = Intent(requireActivity(),AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }

            btnClearData.setOnClickListener {
                prefManager.clear()
            }
        }
        Firebase.observeUsers()
    }
}






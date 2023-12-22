package com.example.uas_happycal.adm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas_happycal.auth.AuthActivity
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.databinding.ActivityProfileAdminBinding

class ProfileAdminActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityProfileAdminBinding.inflate(layoutInflater)
    }
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            val user = Firebase.getUser(prefManager.getEmail())
            txtAdmProfileEmail.text = user?.user_email
            txtAdmProfileName.text = user?.user_name

            btnLogoutAdm.setOnClickListener {
                prefManager.setLoggedIn(false)
                val intentToAuthActivity = Intent(this@ProfileAdminActivity, AuthActivity::class.java)
                startActivity(intentToAuthActivity)
            }

            btnClearDataAdm.setOnClickListener {
            }
        }
    }
}

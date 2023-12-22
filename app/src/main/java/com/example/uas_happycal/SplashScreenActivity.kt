package com.example.uas_happycal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.uas_happycal.adm.AdminActivity
import com.example.uas_happycal.auth.AuthActivity
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.User
import com.example.uas_happycal.user.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    companion object {
        private const val SPLASH_DELAY = 5000L
    }

    private val firestore = FirebaseFirestore.getInstance()

    private val userCollectionRef = firestore.collection("users")
    private val userListLiveData : MutableLiveData<List<User>>
            by lazy {
                MutableLiveData<List<User>>()
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        prefManager = PrefManager.getInstance(this)

        Handler().postDelayed({
            checkLoginStatus()
        }, SPLASH_DELAY)

        observeUsers()
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        val userRole = prefManager.getUserRole()

        if (isLoggedIn && userRole == "admin") {
            val intentToAdminActivity = Intent(this, AdminActivity::class.java)
            startActivity(intentToAdminActivity)
        } else if (isLoggedIn) {
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
        } else {
            Toast.makeText(this, "Silakan login terlebih dahulu!", Toast.LENGTH_SHORT).show()
            val intentToAuthActivity = Intent(this, AuthActivity::class.java)
            startActivity(intentToAuthActivity)
        }
        finish()


    }

    private fun observeUsers() {
        userCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Firebase", "error listening for user changes", error)
                return@addSnapshotListener
            }
            val users = snapshots?.toObjects(User::class.java)
            if (users != null) {
                userListLiveData.postValue(users)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

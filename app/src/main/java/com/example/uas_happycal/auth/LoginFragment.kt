package com.example.uas_happycal.auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.uas_happycal.user.NotifReceiver
import com.example.uas_happycal.R
import com.example.uas_happycal.user.MainActivity
import com.example.uas_happycal.data.User
import com.example.uas_happycal.databinding.FragmentLoginBinding
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.adm.AdminActivity


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    private val channelId = "TEST_NOTIF"
    private val notifId = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Firebase.observeUsers()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        prefManager = PrefManager.getInstance(requireContext())
        return binding.root

        val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnLogin.setOnClickListener {
                val email = edtLoginEmail.text.toString()
                val password = edtLoginPass.text.toString()

                val user = Firebase.getUser(email)

                if (user != null) {
                    if (password == user.user_password) {
                        setLogin(user)
                        if (prefManager.isAdmin() && prefManager.isLoggedIn()) {

                            val intentToAdminActivity = Intent(context, AdminActivity::class.java)
                            startActivity(intentToAdminActivity)
                        } else if (!prefManager.isAdmin() && prefManager.isLoggedIn()) {

                            val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                PendingIntent.FLAG_MUTABLE
                            }
                            else {
                                0
                            }

                            val intent = Intent(context, NotifReceiver::class.java)
                                .putExtra("MESSAGE", "Baca Selengkapnya")
                            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,flag)

                            val buiLder = NotificationCompat.Builder(requireContext(), channelId)
                                .setSmallIcon(R.drawable.happycal_logo)
                                .setContentTitle("HappyCal")
                                .setContentText("Mulai pantau kalorimu sekarang!")
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .addAction(0,"Baca Notif", pendingIntent)

                            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notifChannel = NotificationChannel(channelId, "Notifku", NotificationManager.IMPORTANCE_DEFAULT)
                                with(notificationManager) {
                                    createNotificationChannel(notifChannel)
                                    notify(0, buiLder.build())
                                }
                            }
                            else {
                                notificationManager.notify(0, buiLder.build())
                            }

                            val intentToMainActivity = Intent(context, MainActivity::class.java)
                            startActivity(intentToMainActivity)
                        }
                    } else {
                        showToast("Password tidak sesuai!")
                    }
                } else {
                    showToast("Email $email tidak ditemukan!")
                }
            }
            txtAskToRegist.setOnClickListener {
                (activity as AuthActivity).viewPager2.setCurrentItem(0, true)
            }
        }
    }

    private fun setLogin(user: User) {
        prefManager.saveUserId(user.id)
        prefManager.saveEmail(user.user_email)
        prefManager.savePassword(user.user_password)
        prefManager.saveRole(user.user_role)
        prefManager.setLoggedIn(true)

    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

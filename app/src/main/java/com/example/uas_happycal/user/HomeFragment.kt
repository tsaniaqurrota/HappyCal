package com.example.uas_happycal.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.uas_happycal.R
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import com.example.uas_happycal.databinding.FragmentHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mCustomFoodsDao: FoodDao
    private lateinit var executorService: ExecutorService
    private lateinit var prefManager: PrefManager
    private val channelId = "TEST_NOTIF"
    private val notifId = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager.getInstance(requireContext())
        executorService = Executors.newSingleThreadExecutor()

        val db = FoodRoomDatabase.getDatabase(requireContext())
        mCustomFoodsDao = db!!.foodDao()!!

        val home = Firebase.getUser(prefManager.getEmail())

        with(binding) {
            if (home != null) {
                Log.d("HomeFragment", "User retrieved from Firebase: ${home.user_name}")
                txtName.text = "Hi, ${home.user_name}!"
                txtTargetCal.text = "${home.target_calories.toString().toInt()} cal"

                executorService.execute {
                    val totalCaloriesIn = mCustomFoodsDao.getTotalCalories(prefManager.getUserId())
                    val totalBreakfast =
                        mCustomFoodsDao.getBreakfastCalories(prefManager.getUserId())
                    val totalLunch = mCustomFoodsDao.getLunchCalories(prefManager.getUserId())
                    val totalDinner = mCustomFoodsDao.getDinnerCalories(prefManager.getUserId())
                    val totalSnack = mCustomFoodsDao.getSnackCalories(prefManager.getUserId())
                    val remainingCalories = home.target_calories - totalCaloriesIn

                    txtKaloriMasuk.text = "${totalCaloriesIn.toInt()} cal"
                    txtBreakfastCal.text = "${totalBreakfast.toInt()} cal"
                    txtLunchCal.text = "${totalLunch.toInt()} cal"
                    txtDinnerCal.text = "${totalDinner.toInt()} cal"
                    txtSnackCal.text = "${totalSnack.toInt()} cal"
                    txtSisaCal.text = "${remainingCalories.toInt()} cal"

                    if (remainingCalories <= 200 && remainingCalories > 0) {
                        getNotif(remainingCalories.toString())
                    } else if (remainingCalories.toInt() == 0) {
                        getNotifAchieved(remainingCalories.toString())
                    }
                }

            } else {
                Log.d("HomeFragment", "User not found in Firebase. $txtName")
            }
        }
    }

    fun getNotif(remainingCalories : String) {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }

        val openAppIntent = Intent(context, MainActivity::class.java)
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, flag)

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.happycal_logo)
            .setContentTitle("${remainingCalories} cal menuju target kalori")
            .setContentText("Awas jangan sampai melewati targetmu. Pantau terus konsumsi harianmu!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent)

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "Notifku",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            with(notificationManager) {
                createNotificationChannel(notifChannel)
                notify(0, builder.build())
            }
        } else {
            notificationManager.notify(0, builder.build())
        }
    }

    fun getNotifAchieved(remainingCalories : String) {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }

        val openAppIntent = Intent(context, MainActivity::class.java)
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val openAppPendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, flag)

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.happycal_logo)
            .setContentTitle("Target kalorimu telah tercapai")
            .setContentText("Yuk lihat riwayat konsumsimu hari ini!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppPendingIntent)

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "Notifku",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            with(notificationManager) {
                createNotificationChannel(notifChannel)
                notify(0, builder.build())
            }
        } else {
            notificationManager.notify(0, builder.build())
        }
    }
}
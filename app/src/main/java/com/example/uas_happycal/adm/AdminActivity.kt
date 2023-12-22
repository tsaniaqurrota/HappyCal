package com.example.uas_happycal.adm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_happycal.menu.FoodAdapter
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.databinding.ActivityAdminBinding


class AdminActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            admProfile.setOnClickListener{
                startActivity(Intent(this@AdminActivity, ProfileAdminActivity::class.java))
            }
            btnAddAdmFood.setOnClickListener{
                startActivity(Intent(this@AdminActivity, FoodFormAdminActivity::class.java))
            }
        }
        observeFoods()
        Firebase.getAllFoods()
    }


    private fun observeFoods() {
        Firebase.foodListLiveData.observe(this) { foods ->
            val adapterFood = FoodAdapter(foods) { food ->
                startActivity(
                        Intent(this@AdminActivity, FoodDetailAdmActivity::class.java).putExtra("food", food)
                )
            }
            binding.rvAdmFood.apply {
                adapter = adapterFood
                layoutManager = LinearLayoutManager(this@AdminActivity)
            }
        }
    }
}

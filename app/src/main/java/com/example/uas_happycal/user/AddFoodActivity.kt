package com.example.uas_happycal.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_happycal.menu.FoodAdapter
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.databinding.ActivityAddFoodBinding


class AddFoodActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityAddFoodBinding.inflate(layoutInflater)
    }
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnAddCustomFood.setOnClickListener{
                startActivity(Intent(this@AddFoodActivity, FoodFormActivity::class.java))
            }
        }
        observeFoods()
        Firebase.getAllFoods()
    }

    private fun observeFoods() {
        Firebase.foodListLiveData.observe(this) { foods ->
            val adapterFood = FoodAdapter(foods) { food ->
                startActivity(
                    Intent(this@AddFoodActivity, SelectedFoodFormActivity::class.java).putExtra("food", food)
                )
            }
            binding.rvUserFood.apply {
                adapter = adapterFood
                layoutManager = LinearLayoutManager(this@AddFoodActivity)
            }
        }
    }
}
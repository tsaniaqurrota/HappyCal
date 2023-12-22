package com.example.uas_happycal.adm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.uas_happycal.adm.FoodFormAdminActivity.Companion.EXTRA_FOOD
import com.example.uas_happycal.data.Food
import com.example.uas_happycal.databinding.ActivityFoodDetailAdmBinding

class FoodDetailAdmActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFoodDetailAdmBinding.inflate(layoutInflater)
    }

    lateinit var food: Food

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            food = data?.getSerializableExtra(EXTRA_FOOD) as Food

            with(binding) {
                detailFoodName.setText(food.food_name)
                detailFoodCal.setText(food.food_cal.toString())
                Glide.with(this@FoodDetailAdmActivity)
                    .load(food.food_pict) // Assuming food.food_pict is the URL or resource ID of the image
                    .into(detailImgFood)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            food = intent.getSerializableExtra("food") as Food
            detailFoodName.setText(food.food_name)
            detailFoodCal.setText(food.food_cal.toString())
            Glide.with(this@FoodDetailAdmActivity)
                .load(food.food_pict)
                .into(detailImgFood)

            btnUpdate.setOnClickListener{
                val intentToFormAdminActivity = Intent(this@FoodDetailAdmActivity, FoodFormAdminActivity::class.java).putExtra("food", food)
                launcher.launch(intentToFormAdminActivity)
            }

            btnDelete.setOnClickListener{
                DeleteFoodFragment(food).show(supportFragmentManager,"DELETE_DIALOG")
            }
        }
    }
}
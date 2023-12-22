package com.example.uas_happycal.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.uas_happycal.R
import com.example.uas_happycal.adm.DeleteFoodFragment
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.AteFood
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import com.example.uas_happycal.databinding.ActivityDetailHistoryBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailHistoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailHistoryBinding.inflate(layoutInflater)
    }
    private lateinit var mFoodsCustomDao: FoodDao
    private lateinit var executorService: ExecutorService
    private lateinit var prefManager: PrefManager

    companion object {
        const val UPDATE_TASK_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = FoodRoomDatabase.getDatabase(this)
        mFoodsCustomDao = db!!.foodDao()!!

        with(binding) {

            val ateFood = intent.getSerializableExtra("ateFood") as AteFood
            detailHcFoodName.setText(ateFood.food_name)
            detailHcFoodCal.setText("${ateFood.food_cal.toInt().toString()} cal")
            detailHcFoodTakaran.setText("${ateFood.food_gr.toInt().toString()} gr")
            detailHcFoodEatTime.setText(ateFood.food_eat_time)
            detailDescFood.setText(ateFood.food_desc)
            detailHcFoodJenis.setText(ateFood.food_type)
            Glide.with(this@DetailHistoryActivity)
                .load(ateFood.food_pict)
                .into(detailHcImgFood)

            btnUpdateHcFood.setOnClickListener{
                val intentToFormTaskActivity = Intent(this@DetailHistoryActivity, FoodFormActivity::class.java)
                intentToFormTaskActivity.putExtra("ateFood", ateFood)
                startActivityForResult(intentToFormTaskActivity, UPDATE_TASK_REQUEST_CODE)
            }

            btnDeleteHcFood.setOnClickListener{
                DeleteHistoryFragment(ateFood).show(supportFragmentManager,"DELETE_DIALOG")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            val editedAteFood = data?.getSerializableExtra("editedAteFood") as? AteFood

            if (editedAteFood != null) {
                with(binding) {
                    detailHcFoodName.setText(editedAteFood.food_name)
                    detailHcFoodCal.setText("${editedAteFood.food_cal.toString().toInt()} cal")
                    detailHcFoodTakaran.setText("${editedAteFood.food_gr.toString().toInt()} gr")
                    detailHcFoodEatTime.setText(editedAteFood.food_eat_time)
                    detailHcFoodJenis.setText(editedAteFood.food_type)
                    Glide.with(this@DetailHistoryActivity)
                        .load(editedAteFood.food_pict)
                        .into(detailHcImgFood)
                }
            }
        }
    }
}

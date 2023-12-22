package com.example.uas_happycal.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.uas_happycal.R
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.AteFood
import com.example.uas_happycal.data.Food
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import com.example.uas_happycal.databinding.ActivitySelectedFoodFormBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SelectedFoodFormActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySelectedFoodFormBinding.inflate(layoutInflater)
    }

    private lateinit var mFoodsCustomDao: FoodDao
    private lateinit var executorService: ExecutorService
    private lateinit var prefManager: PrefManager


    private val food_type = arrayOf(
        "Sarapan",
        "Makan Siang",
        "Makan Malam",
        "Lainnya"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        executorService = Executors.newSingleThreadExecutor()
        val db = FoodRoomDatabase.getDatabase(this)
        mFoodsCustomDao = db!!.foodDao()!!

        with(binding) {
            val foodTypeAdapter = ArrayAdapter<String>(
                this@SelectedFoodFormActivity,
                R.layout.spinner_item,
                food_type
            )
            foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFoodType.adapter = foodTypeAdapter

            spinnerFoodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedType = food_type[position]
                    Toast.makeText(
                        this@SelectedFoodFormActivity, selectedType,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            val food = intent.getSerializableExtra("food") as Food
            edtFoodName.setText(food.food_name)
            edtFoodTakaran.setText(food.food_gr.toString())
            edtFoodCal.setText(food.food_cal.toString())
            edtImgFood.setText(food.food_pict)


            btnAddFoodToHistory.setOnClickListener {
                val a = (edtFoodTakaran.text.toString().toDouble() * food.food_cal) / 100


                val foodFix = AteFood(
                    userId = prefManager.getUserId(),
                    food_name = edtFoodName.text.toString(),
                    food_gr = edtFoodTakaran.text.toString().toDouble(),
                    food_cal = a,
                    food_pict = food.food_pict,
                    food_eat_time = edtEatTime.text.toString(),
                    food_type = spinnerFoodType.selectedItem.toString(),
                    food_desc = edtDescFood.text.toString()
                )
                insertAteFood(foodFix)
                val intentToHistoryFragment = Intent(this@SelectedFoodFormActivity, MainActivity::class.java)
                startActivity(intentToHistoryFragment)
            }
        }
    }

    private fun insertAteFood(ateFood: AteFood) {
        executorService.execute {mFoodsCustomDao.insert(ateFood)}
    }

    private fun updateAteFood(ateFood: AteFood) {
        executorService.execute {  mFoodsCustomDao.update(ateFood)}
    }
}
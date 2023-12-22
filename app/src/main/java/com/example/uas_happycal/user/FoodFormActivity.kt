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
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import com.example.uas_happycal.databinding.ActivityFoodFormBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FoodFormActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityFoodFormBinding.inflate(layoutInflater)
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

            val foodTypeAdapter = ArrayAdapter<String>(this@FoodFormActivity, R.layout.spinner_item, food_type )
            foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFoodType.adapter = foodTypeAdapter

            spinnerFoodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        // Cek apakah sudah terdapat ekstra food dalam intent
            if (intent.hasExtra("ateFood")) {
                // Jika sudah, akan menjadi pengeditan
                val ateFood = intent.getSerializableExtra("ateFood") as AteFood

                // Mengisi formulir dengan data food yang ada
                edtFoodName.setText(ateFood.food_name)
                edtFoodTakaran.setText(ateFood.food_gr.toString())
                edtFoodCal.setText(ateFood.food_cal.toString())
                edtImgFood.setText(ateFood.food_pict)
                edtEatTime.setText(ateFood.food_eat_time)
                edtDescFood.setText(ateFood.food_desc)
                val selectedType = food_type.indexOf(ateFood.food_eat_time)
                spinnerFoodType.setSelection(selectedType)

                btnAddFoodToHistory.setOnClickListener {
                    //Membuat object food yg telah diubah
                    val editedAteFood = AteFood(
                        id = ateFood.id,
                        userId = prefManager.getUserId(),
                        food_name = edtFoodName.text.toString(),
                        food_gr = edtFoodTakaran.text.toString().toDouble(),
                        food_cal = edtFoodCal.text.toString().toDouble(),
                        food_pict = edtImgFood.text.toString(),
                        food_eat_time = edtEatTime.text.toString(),
                        food_type = spinnerFoodType.selectedItem.toString(),
                        food_desc = edtDescFood.text.toString()
                    )
                    updateAteFood(editedAteFood)
                    val intentToMainActivity = Intent(this@FoodFormActivity, MainActivity::class.java)
                    startActivity(intentToMainActivity)
                    finish()
                }
            } else {
                //Jika belum ada ekstra food, akan dibuat food baru
                btnAddFoodToHistory.setOnClickListener {
                    //Cek apakah inputan telah diisi
                    if (edtFoodName.text.toString() != "" && edtFoodTakaran.text.toString() != "" && edtFoodCal.text.toString() != "" && edtImgFood.text.toString() != ""
                        && edtEatTime.text.toString() !== ""
                    ) {
                        // Jika telah terisi, object  baru akan dibuat
                        val newFoodHistory = AteFood(
                            userId = prefManager.getUserId(),
                            food_name = edtFoodName.text.toString(),
                            food_gr = edtFoodTakaran.text.toString().toDouble(),
                            food_cal = edtFoodCal.text.toString().toDouble(),
                            food_pict = edtImgFood.text.toString(),
                            food_eat_time = edtEatTime.text.toString(),
                            food_desc = edtDescFood.text.toString(),
                            food_type = spinnerFoodType.selectedItem.toString()
                        )
                        // Menyimpan food baru ke database
                        insertAteFood(newFoodHistory)
                        Toast.makeText(this@FoodFormActivity, "Makanan baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@FoodFormActivity,
                            "Kolom tidak boleh kosong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun insertAteFood(ateFood: AteFood) {
        executorService.execute{ mFoodsCustomDao.insert(ateFood) }
    }

    private fun updateAteFood(ateFood: AteFood) {
        executorService.execute { mFoodsCustomDao.update(ateFood) }
    }

}
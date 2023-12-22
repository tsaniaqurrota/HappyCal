package com.example.uas_happycal.adm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.data.Food
import com.example.uas_happycal.databinding.ActivityFoodFormAdminBinding

class FoodFormAdminActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityFoodFormAdminBinding.inflate(layoutInflater)
    }

    companion object {
        const val EXTRA_FOOD = "extra_food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            if (intent.hasExtra("food")) {
                val food = intent.getSerializableExtra("food") as Food
                edtFoodNameAdm.setText(food.food_name)
                edtFoodGrAdm.setText(food.food_gr.toString())
                edtFoodCalAdm.setText(food.food_cal.toString())
                edtFoodPictAdm.setText(food.food_pict)

                btnAddFoodAdm.setOnClickListener {
                    val editedFood = Food(
                        id = food.id,
                        food_name = edtFoodNameAdm.text.toString(),
                        food_gr = edtFoodGrAdm.text.toString().toDouble(),
                        food_cal = edtFoodCalAdm.text.toString().toDouble(),
                        food_pict = edtFoodPictAdm.text.toString()
                        )
                    // Memastikan id food tidak kosong sebelum memperbarui food
                    if (food.id.isNotEmpty()) {
                        // Memperbarui food di database
                        Firebase.updateFood(editedFood)
                        //Mengirim kembali data ke AdminActivity
                        val intent = Intent()
                        intent.putExtra(EXTRA_FOOD, editedFood)
                        setResult(Activity.RESULT_OK, intent)
                        //Menutup activity
                        finish()
                    }
                }
            } else {
                //Jika belum ada ekstra food, akan dibuat food baru
                btnAddFoodAdm.setOnClickListener {
                    //Cek apakah inputan telah diisi
                    if (edtFoodNameAdm.text.toString() != "" && edtFoodGrAdm.text.toString() != "" && edtFoodCalAdm.text.toString() != ""
                        ) {
                        // Jika telah terisi, object Food baru akan dibuat
                        val newFood = Food(
                            food_name = edtFoodNameAdm.text.toString(),
                            food_gr = edtFoodGrAdm.text.toString().toDouble(),
                            food_cal = edtFoodCalAdm.text.toString().toDouble(),
                            food_pict = edtFoodPictAdm.text.toString()
                        )
                        // Menyimpan food baru ke database
                        Firebase.addFood(newFood)
                        Toast.makeText(this@FoodFormAdminActivity, "Makanan baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        val intentToAdminActivity = Intent(this@FoodFormAdminActivity, AdminActivity::class.java)
                        startActivity(intentToAdminActivity)
                    } else {
                        // Jika ada input yang kosong, akan ditampilkan pesan peringatan
                        Toast.makeText(
                            this@FoodFormAdminActivity,
                            "Kolom tidak boleh kosong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
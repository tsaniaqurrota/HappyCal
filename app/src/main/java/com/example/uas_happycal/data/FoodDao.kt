package com.example.uas_happycal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ateFood: AteFood)

    @Update
    fun update(ateFood: AteFood)

    @Delete
    fun delete(ateFood: AteFood)

    @Query("SELECT * FROM custom_food_table WHERE user_id = :userId")
    fun getCustomFoodLiveData(userId: String): LiveData<List<AteFood>>

    @get:Query("SELECT * FROM custom_food_table ORDER BY id ASC")
    val allCustomFoods: LiveData<List<AteFood>>

    @Query("SELECT SUM(r_food_cal) FROM custom_food_table WHERE user_id = :userId")
    fun getTotalCalories(userId: String): Double

    @Query("SELECT SUM(r_food_cal) FROM custom_food_table WHERE user_id = :userId AND r_food_type = 'Sarapan'")
    fun getBreakfastCalories(userId: String): Double

    @Query("SELECT SUM(r_food_cal) FROM custom_food_table WHERE user_id = :userId AND r_food_type = 'Makan Siang'")
    fun getLunchCalories(userId: String): Double

    @Query("SELECT SUM(r_food_cal) FROM custom_food_table WHERE user_id = :userId AND r_food_type = 'Makan Malam'")
    fun getDinnerCalories(userId: String): Double

    @Query("SELECT SUM(r_food_cal) FROM custom_food_table WHERE user_id = :userId AND r_food_type = 'Lainnya'")
    fun getSnackCalories(userId: String): Double
}
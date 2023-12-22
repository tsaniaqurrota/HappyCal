package com.example.uas_happycal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "custom_food_table")

data class AteFood(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

//    @ColumnInfo(name = "food_id")
//    val foodId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "r_food_name")
    var food_name: String = "",

    @ColumnInfo(name = "r_food_takaran")
    var food_gr: Double = 0.0,

    @ColumnInfo(name = "r_food_cal")
    var food_cal: Double = 0.0,

    @ColumnInfo(name = "r_food_pict")
    var food_pict: String = "https://drive.google.com/file/d/1yRMZutqmkQFf2WwPaVj8--Sq0lV0c94r/view?usp=sharing",

    @ColumnInfo(name = "r_food_eat_time")
    var food_eat_time: String = "",

    @ColumnInfo(name = "r_food_type")
    var food_type: String = "",

    @ColumnInfo(name = "r_food_desc")
    var food_desc: String = "",


    ) : Serializable

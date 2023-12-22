package com.example.uas_happycal.data

import java.io.Serializable

data class Food(
    var id: String = "",
    var food_name : String = "",
    var food_gr : Double = 0.0,
    var food_cal : Double = 0.0,
    var food_pict : String = "",
) : Serializable

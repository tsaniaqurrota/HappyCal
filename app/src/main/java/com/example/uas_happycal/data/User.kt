package com.example.uas_happycal.data

import java.io.Serializable

data class User(
    var id: String = "",
    var user_email : String = "",
    var user_password : String = "",
    var user_cpassword : String = "",
    var user_role : String = "user",
    val user_name: String = "",
    val user_height: Double = 0.0,
    val user_weight: Double = 0.0,
    val target_calories: Int = 0
    ) : Serializable

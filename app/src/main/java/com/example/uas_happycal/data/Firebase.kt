package com.example.uas_happycal.data

import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

object Firebase {
    private val userCollectionRef = FirebaseFirestore.getInstance().collection("users")
    val userListLiveData : MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }

    private val foodCollectionRef = FirebaseFirestore.getInstance().collection("tasks")
    val foodListLiveData : MutableLiveData<List<Food>> by lazy {
        MutableLiveData<List<Food>>()
    }

    fun getUser(email: String): User? {
        val userList = userListLiveData.value

        Log.d("Firebase", "Attempting to get user with email: $email")

        if (userList != null) {
            for (user in userList) {
                if (email == user.user_email) {
                    Log.d("Firebase", "User found with email: $email")
                    return user
                }
            }
        }
        Log.d("Firebase", "User not found with email: $email")
        return null
    }

    fun addUser(user : User) {
        userCollectionRef.add(user).addOnSuccessListener { documentReference ->
            user.id = documentReference.id
            documentReference.set(user).addOnFailureListener {
                Log.d("Firebase", "Error adding user id: ", it)
            }
        }
    }

    fun addFood(food : Food) {
        foodCollectionRef.add(food).addOnSuccessListener { documentReference ->
            food.id = documentReference.id
            documentReference.set(food).addOnFailureListener {
                Log.d("Firebase", "Error adding food id: ", it)
            }
        }
    }

    fun updateFood(food: Food) {
        foodCollectionRef.document(food.id).set(food).addOnFailureListener {
            Log.d("Firebase", "Error updating food", it)
        }
    }

    fun observeUsers() {
        userCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Firebase", "error listening for user changes", error)
            }
            val users = snapshots?.toObjects(User::class.java)
            if (users != null) {
                userListLiveData.postValue(users)
            }
        }
    }

    fun getAllFoods() {
        observeFoodsChange()
    }

    fun observeFoodsChange() {
        foodCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d(
                    "MainActivity",
                    "Error Listening for task changes:", error
                )
            }
            val foods = snapshots?.toObjects(Food::class.java)
            if (foods != null) {
                foodListLiveData.postValue(foods)
            }
        }
    }

    fun deleteTask(food: Food) {
        // Menghapus dokumen task dengan id yang sesuai dari koleksi "tasks"
        foodCollectionRef.document(food.id)
            .delete()
            .addOnSuccessListener {
                Log.d("Delete", "Food deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Delete", "Error deleting food", e)
            }
    }
}
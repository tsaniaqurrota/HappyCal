package com.example.uas_happycal.adm

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.uas_happycal.data.Firebase
import com.example.uas_happycal.data.Food
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DeleteFoodFragment(private val food: Food) : DialogFragment() {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Ingin menghapus menu ini?")
                .setPositiveButton("Ya") { dialog, id ->
                    deleteTask()
                    (activity as? FoodDetailAdmActivity)?.finish()

                }
                .setNegativeButton("Batal") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteTask() {
        executorService.execute {
            Firebase.deleteTask(food)
        }
    }
}

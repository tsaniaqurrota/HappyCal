package com.example.uas_happycal.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.uas_happycal.data.AteFood
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DeleteHistoryFragment(private val ateFood: AteFood) : DialogFragment() {

    private lateinit var mFoodsCustomDao: FoodDao
    private lateinit var executorService: ExecutorService

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        executorService = Executors.newSingleThreadExecutor()
        val db = FoodRoomDatabase.getDatabase(requireContext())
        mFoodsCustomDao = db!!.foodDao()!!

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Ingin menghapus menu ini?")
                .setPositiveButton("Ya") { dialog, id ->
                    delete(ateFood)
                    (activity as? DetailHistoryActivity)?.finish()
                }
                .setNegativeButton("Batal") { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun delete(ateFood: AteFood) {
        executorService.execute { mFoodsCustomDao.delete(ateFood) }
    }
}

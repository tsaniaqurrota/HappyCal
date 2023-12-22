package com.example.uas_happycal.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_happycal.auth.PrefManager
import com.example.uas_happycal.data.FoodDao
import com.example.uas_happycal.data.FoodRoomDatabase
import com.example.uas_happycal.databinding.FragmentHistoryBinding
import com.example.uas_happycal.menu.HistoryAdapter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mCustomFoodsDao: FoodDao
    private lateinit var executorService: ExecutorService
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = FoodRoomDatabase.getDatabase(requireContext())
        mCustomFoodsDao = db!!.foodDao()!!

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager.getInstance(requireContext())

        with(binding) {
            btnAddFood.setOnClickListener {
                val intentToAddFoodActivity = Intent(context, AddFoodActivity::class.java)
                startActivity(intentToAddFoodActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllCustomFoods()
    }

    private fun getAllCustomFoods() {
        mCustomFoodsDao.getCustomFoodLiveData(prefManager.getUserId()).observe(viewLifecycleOwner) {ateFoods ->
            val adapterHistory = HistoryAdapter(ateFoods) { ateFood ->
                startActivity(
                    Intent(context, DetailHistoryActivity::class.java).putExtra("ateFood", ateFood)
                )
            }
            binding.rvHistory.apply {
                adapter = adapterHistory
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}
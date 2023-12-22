package com.example.uas_happycal.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.uas_happycal.R
import com.example.uas_happycal.data.AteFood
import com.example.uas_happycal.data.Food
import com.example.uas_happycal.databinding.ItemHistoryBinding

typealias OnClickHistory = (AteFood) ->Unit

class HistoryAdapter (
    private var listsCustomFood: List<AteFood>,
    private val onClickHistory: OnClickHistory
):

    RecyclerView.Adapter<HistoryAdapter.ItemHistoryViewHolder>() {

    fun updateData(newCustomFoods: List<AteFood>): Int {
        listsCustomFood = newCustomFoods
        notifyDataSetChanged()
        return listsCustomFood.size
    }
    inner class ItemHistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ateFood: AteFood) {
            with(binding) {
                txtFoodName.text = ateFood.food_name
                txtEatTime.text = ateFood.food_eat_time
                txtFoodCal.text = "${ateFood.food_cal.toInt().toString()} cal"

                Glide.with(itemView.context).asBitmap().load(ateFood.food_pict)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(imgFood)

                if (ateFood.food_type == "Makan Siang") {
                    foodStatus.setImageResource(R.drawable.icon_day)
                } else if (ateFood.food_type == "Sarapan") {
                    foodStatus.setImageResource(R.drawable.icon_sunrise)
                } else if (ateFood.food_type == "Makan Malam") {
                    foodStatus.setImageResource(R.drawable.icon_night)
                } else {
                    foodStatus.setImageResource(R.drawable.icon_snack)
                }

                itemView.setOnClickListener {
                    onClickHistory(ateFood)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listsCustomFood.size
    }

    override fun onBindViewHolder(holder: ItemHistoryViewHolder, position: Int) {
        holder.bind(listsCustomFood[position])
    }
}
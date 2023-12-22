package com.example.uas_happycal.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.uas_happycal.data.Food
import com.example.uas_happycal.databinding.ItemFoodAdmBinding

typealias OnClickFood = (Food) ->Unit

class FoodAdapter (
    private var listsFood: List<Food>,
    private val onClickFood: OnClickFood
):

    RecyclerView.Adapter<FoodAdapter.ItemFoodViewHolder>() {

    fun updateData(newFood: List<Food>): Int {
        listsFood = newFood
        notifyDataSetChanged()
        return listsFood.size
    }

    inner class ItemFoodViewHolder(
        private val binding: ItemFoodAdmBinding
    ) :

        RecyclerView.ViewHolder(binding.root) {

        // Mengambil data food ke tampilan
        fun bind(food: Food) {
            with(binding) {
                txtAdmFoodName.text = food.food_name
                txtAdmFoodCal.text = "${food.food_cal.toInt()} cal/100 gr"
                Glide.with(itemView.context).asBitmap().load(food.food_pict)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(imgAdmFood)
                itemView.setOnClickListener {
                    onClickFood(food)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFoodViewHolder {
        val binding = ItemFoodAdmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listsFood.size
    }

    override fun onBindViewHolder(holder: ItemFoodViewHolder, position: Int) {
        holder.bind(listsFood[position])
    }


}
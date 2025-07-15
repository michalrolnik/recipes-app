package com.example.foodbook.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.FragmentFavoritesBinding
import com.example.foodbook.databinding.PopularItemsBinding
import com.example.foodbook.pojo.MealsByCategory

class FavoriteAdapter (): RecyclerView.Adapter<FavoriteAdapter.FavoriteMealViewHolder>(){

    lateinit var onItemClick:((MealsByCategory)-> Unit)
    private var mealsList = ArrayList<MealsByCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsList:ArrayList<MealsByCategory>){
        this.mealsList=mealsList
        notifyDataSetChanged()
    }

    class FavoriteMealViewHolder(val binding: FragmentFavoritesBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
        return FavoriteMealViewHolder(FragmentFavoritesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
//        Glide.with(holder.itemView)
//            .load(mealsList[position].strMealThumb)
//            .into(holder.binding.imgPopularMealItem)


        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

    }


}
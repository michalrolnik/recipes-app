package com.example.foodbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.CategoryItemBinding
import com.example.foodbook.pojo.Category


class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoryList: List<Category> = ArrayList<Category>()

    var onItemClick: ((Category) -> Unit)? = null

    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }


    class CategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)

        holder.binding.tvCategoryName.text = categoryList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(categoryList[position])
        }

    }


    override fun getItemCount(): Int {
        return categoryList.size
    }


}
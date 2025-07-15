package com.example.foodbook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodbook.R
import com.example.foodbook.adapters.CategoryMealsAdapter
import com.example.foodbook.databinding.ActivityCategoryMealsBinding
import com.example.foodbook.databinding.ActivityMealBinding
import com.example.foodbook.fragments.HomeFragment
import com.example.foodbook.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this).get(CategoryMealsViewModel::class.java)

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealList->
            binding.tvCategoryCount.text = mealList.size.toString()
            categoryMealsAdapter.setMealsList(mealList)
        })

    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply{
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter

        }
    }
}
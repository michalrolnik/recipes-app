package com.example.foodbook.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodbook.db.MealDatabase
import com.example.foodbook.pojo.Category
import com.example.foodbook.pojo.CategoryList
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealList
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.pojo.MealsByCategoryList
import com.example.foodbook.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()

    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()

    private var categoriesLiveData = MutableLiveData<List<Category>>()

    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    private var searchdMealLiveData = MutableLiveData<List<Meal>>()

    private val newMealLiveData = MutableLiveData<Meal>()


    init{
        getRandomMeal()
    }

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }


    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }



    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let{
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }


    fun searchdMeals(searchQuery: String) = RetrofitInstance.api.searchMeal(searchQuery).enqueue(object :Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealsList = response.body()?.meals
            mealsList?.let{
                searchdMealLiveData.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("HomeViewModel", t.message.toString())
        }

    })

    fun observeSearchedMealLiveData():LiveData<List<Meal>> = searchdMealLiveData


    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }


    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }


    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData():LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }

    fun observeBottomSheetMealLiveData():LiveData<Meal> = bottomSheetMealLiveData


    fun observeNewMealLiveData(): LiveData<Meal> = newMealLiveData

    fun setNewMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }



}
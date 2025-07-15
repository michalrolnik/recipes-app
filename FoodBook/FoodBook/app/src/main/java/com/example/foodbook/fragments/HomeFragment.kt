package com.example.foodbook.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.activities.CategoryMealsActivity
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.activities.MealActivity
import com.example.foodbook.adapters.CategoriesAdapter
import com.example.foodbook.adapters.MostPopularAdapter
import com.example.foodbook.databinding.FragmentHomeBinding
import com.example.foodbook.fragments.bottomsheet.MealBottomSheetFragment
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.foodbook.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodbook.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodbook.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodbook.fragments.categoryName"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel= (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()


        prepareCategoriesRecyclerView()

        viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()


    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter =categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            //  val temp = meal.meals[0]
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            Observer { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal
            })
    }
}




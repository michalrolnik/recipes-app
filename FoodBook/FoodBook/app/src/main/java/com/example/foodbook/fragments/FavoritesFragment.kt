package com.example.foodbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.adapters.MealsAdapter
import com.example.foodbook.databinding.FragmentFavoritesBinding
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment :Fragment(){

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: MealsAdapter
    private var FavoritesItemsLiveData = MutableLiveData<List<MealsByCategory>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPopularItems()

        prepareRecyclerView()
        observeFavorite()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList[position]

                viewModel.deleteMeal(deletedMeal)

                Snackbar.make(requireView(),getString(R.string.meal_deleted), Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        // Use a coroutine to insert the meal back
                        viewModel.insertMeal(deletedMeal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)



    }



    private fun prepareRecyclerView() {
        favoritesAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavorite() { //requireActivity()
        viewModel.observeFavoriteMealsLiveData().observe(viewLifecycleOwner, Observer { meals->
            favoritesAdapter.differ.submitList(meals)
        })
    }






}

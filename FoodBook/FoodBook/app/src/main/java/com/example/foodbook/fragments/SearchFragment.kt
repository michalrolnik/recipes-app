package com.example.foodbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.adapters.CategoriesAdapter
import com.example.foodbook.adapters.MealsAdapter
import com.example.foodbook.databinding.FragmentSearchBinding
import com.example.foodbook.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imgSearchArrow.setOnClickListener{
            searchMeals()
        }


        observeSearchMealLiveData()

        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener{
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchdMeals(it.toString())
            }

        }

    }

    private fun observeSearchMealLiveData() {
        viewModel.observeSearchedMealLiveData().observe(viewLifecycleOwner, Observer { mealsList->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchdMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecyclerViewAdapter
        }
    }
}
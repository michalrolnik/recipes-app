package com.example.foodbook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodbook.R
import com.example.foodbook.db.MealDatabase
import com.example.foodbook.viewModel.HomeViewModel
import com.example.foodbook.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {



    val viewModel: HomeViewModel by lazy{
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.frag_host)

        NavigationUI.setupWithNavController(bottomNavigation,navController)


    }


}
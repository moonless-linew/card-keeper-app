package com.example.cardapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cardapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val bottomNavigation = binding.bottomNavigation
        val navHostFragment = binding.hostFragment.getFragment() as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
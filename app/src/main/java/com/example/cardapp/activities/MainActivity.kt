package com.example.cardapp.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.ActivityMainBinding
import com.example.cardapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!
    val userData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupNavigation()

    }
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activityNavigationHost) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.flow_graph)
        when {
            userData -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
            !userData -> {
                navGraph.setStartDestination(R.id.authFlowFragment)
            }
        }
        navController.graph = navGraph
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }
}
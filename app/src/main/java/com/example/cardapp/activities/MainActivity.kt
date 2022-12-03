package com.example.cardapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.cardapp.R
import com.example.cardapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var auth: FirebaseAuth

    //this is a single activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        setupNavigation()

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activityNavigationHost) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.flow_graph)
        when {
            auth.currentUser != null -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
            auth.currentUser == null -> {
                navGraph.setStartDestination(R.id.authFlowFragment)
            }
        }
        navController.graph = navGraph
    }


}
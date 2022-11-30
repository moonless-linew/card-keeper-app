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
    //now it single activity
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



        val navHostFragment = binding.hostFragment.getFragment() as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.root)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navigationView.also {
            it.inflateHeaderView(R.layout.header_drawer)
            it.setupWithNavController(navController)
            it.getHeaderView(0)
                .findViewById<View>(R.id.imageView)
                .setOnClickListener{
                    navController.navigate(R.id.userFragment)
                    binding.root.close()
                }
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onStart() {
        val currentUser = auth.currentUser
        if(currentUser != null){
            user = currentUser as User;
        }
        super.onStart()
    }
}
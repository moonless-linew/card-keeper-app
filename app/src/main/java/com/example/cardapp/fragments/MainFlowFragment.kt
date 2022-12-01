package com.example.cardapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentMainBinding

class MainFlowFragment: ParentFlowFragment(
    R.layout.fragment_main, R.id.hostFragment
){
    private val binding by viewBinding(FragmentMainBinding::bind)


    override fun setupNavigation(navController: NavController) {
        binding.navigationView.setupWithNavController(navController)
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
}
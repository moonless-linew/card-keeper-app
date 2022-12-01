package com.example.cardapp.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class ParentFlowFragment(
    @LayoutRes layoutId: Int,
    @IdRes private val navHostFragmentId: Int
): Fragment(layoutId){
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment: NavHostFragment = childFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        setupNavigation(navController)
    }
    protected open fun setupNavigation(navController: NavController){

    }

}
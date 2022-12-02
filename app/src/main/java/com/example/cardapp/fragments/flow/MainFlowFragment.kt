package com.example.cardapp.fragments.flow


import android.view.View

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentMainBinding
import com.example.cardapp.viewmodels.MainFlowFragmentViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainFlowFragment : ParentFlowFragment(
    R.layout.fragment_main, R.id.hostFragment
) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainFlowFragmentViewModel by viewModels()


    override fun setupNavigation(navController: NavController) {
        viewModel.downloadUser(Firebase.auth.uid.toString())
        binding.navigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.root)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navigationView.also {
            it.inflateHeaderView(R.layout.header_drawer)
            it.setupWithNavController(navController)
            it.getHeaderView(0)
                .findViewById<View>(R.id.imageView)
                .setOnClickListener {
                    navController.navigate(R.id.userFragment)
                    binding.root.close()
                }
        }
        viewModel.user.observe(requireActivity()) {
            binding
                .navigationView
                .getHeaderView(0)
                .findViewById<TextView>(R.id.nameText)
                .text = it.name
        }
    }

}
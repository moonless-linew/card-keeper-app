package com.example.cardapp.fragments.flow


import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager

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
        hideKeyBoard()
        viewModel.downloadUser(Firebase.auth.uid.toString())
        binding.navigationView.setupWithNavController(navController)
        AppBarConfiguration(navController.graph, binding.root).also {
            binding.toolbar.setupWithNavController(navController, it)
        }
        binding.navigationView.also {
            it.inflateHeaderView(R.layout.header_drawer)
            it.setupWithNavController(navController)
            //Open user fragment with image in header of drawer
            it.getHeaderView(0)
                .findViewById<View>(R.id.imageView)
                .setOnClickListener {
                    navController.navigate(R.id.userFragment)
                    binding.root.close()
                }
            //Configure share item to run share activity
            it.menu.findItem(R.id.shareFragment).setOnMenuItemClickListener {
                runShareActivity()
                true
            }
        }

    }

    override fun setupListeners() {
        viewModel.user.observe(viewLifecycleOwner) {
            binding
                .navigationView
                .getHeaderView(0)
                .findViewById<TextView>(R.id.nameText)
                .text = it?.name
        }
    }

    private fun runShareActivity() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.disk_link))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun hideKeyBoard() {
        (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).also {
            it.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }


}
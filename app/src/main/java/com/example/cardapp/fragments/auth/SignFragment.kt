package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.adapters.IntroductionViewPagerAdapter
import com.example.cardapp.databinding.FragmentSignBinding


class SignFragment : Fragment() {
    private var _binding: FragmentSignBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignBinding.inflate(layoutInflater, container, false)
        setupClickLogWithPhone()
        setupClickLogWithName()
        setupViewPager()
        return binding.root
    }

    private fun setupClickLogWithPhone() {
        binding.phoneButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signFragment_to_phoneFragment)
        }
    }

    private fun setupClickLogWithName() {
        binding.registerButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signFragment_to_nameFragment)
        }

    }

    private fun setupViewPager() {

        binding.viewPager2.also {
            it.adapter = IntroductionViewPagerAdapter(this)
            binding.tabDots.attachTo(it)

        }

    }


    private fun NavController.navigateSafely(@IdRes actionId: Int) {
        currentDestination?.getAction(actionId)?.let { navigate(actionId) }
    }
}





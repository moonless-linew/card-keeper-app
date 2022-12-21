package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.adapters.IntroductionViewPagerAdapter
import com.example.cardapp.databinding.FragmentSignBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.utils.SlidesUtils


class SignFragment : Fragment() {
    private var _binding: FragmentSignBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignBinding.inflate(layoutInflater, container, false)
        setupPhoneButton()
        setupNameButton()
        setupViewPager()
        return binding.root
    }

    private fun setupPhoneButton() {
        binding.phoneButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signFragment_to_phoneFragment)
        }
    }

    private fun setupNameButton() {
        binding.registerButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_signFragment_to_nameFragment)
        }

    }

    private fun setupViewPager() {
        binding.viewPager2.also {
            it.adapter = IntroductionViewPagerAdapter(
                this,
                SlidesUtils.SLIDES
            )
            binding.tabDots.attachTo(it)
        }
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}





package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentRegisterBinding


class PhoneFragment: Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        setupClickSuccess()
        setupClickFail()
        return binding.root
    }



    private fun setupClickSuccess() {
        binding.successButton.setOnClickListener {
            activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
        }
    }

    private fun setupClickFail() {
        binding.failButton.setOnClickListener {
            Toast.makeText(requireActivity(), "FAIL", Toast.LENGTH_SHORT).show()
        }

    }


    private fun Fragment.activityNavController() = requireActivity().findNavController(R.id.activityNavigationHost)

    private fun NavController.navigateSafely(@IdRes actionId: Int) {
        currentDestination?.getAction(actionId)?.let { navigate(actionId) }
    }

}
package com.example.cardapp.presentation.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentNameBinding
import com.example.cardapp.extensions.activityNavController
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.presentation.viewmodels.NameFragmentViewModel
import com.example.cardapp.presentation.model.status.NameAuthStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameFragment : Fragment(R.layout.fragment_name) {
    private val viewModel: NameFragmentViewModel by viewModels()
    private val binding by viewBinding(FragmentNameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Firebase.auth.currentUser == null) {
            binding.nameContinueButton.setOnClickListener {
                nameAuth()
            }
        } else {
            binding.nameContinueButton.setOnClickListener {
                phoneAuth()
            }
        }

        setupAuthStatusObserver()
    }

    private fun phoneAuth() {
        if (binding.editName.text.toString().length in 1..30) {
            startLoading()
            viewModel.registerUserWithPhone(binding.editName.text.toString())
        }
        else toastError(getString(R.string.name_error))
    }

    private fun nameAuth() {
        if (binding.editName.text.toString().length in 1..30) {
            startLoading()
            viewModel.registerUserWithName(binding.editName.text.toString())
        }
        else toastError(getString(R.string.name_error))
    }

    private fun setupAuthStatusObserver() {
        viewModel.authStatus.observe(viewLifecycleOwner) {
            when (it) {
                NameAuthStatus.Success -> {
                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                }
                NameAuthStatus.InternetError -> toastError(getString(R.string.internet_error))
            }
            stopLoading()
        }
    }

    private fun startLoading() {
        binding.nameProgressBar.visibility = View.VISIBLE
        binding.nameContinueButton.visibility = View.INVISIBLE
    }

    private fun stopLoading() {
        binding.nameProgressBar.visibility = View.INVISIBLE
        binding.nameContinueButton.visibility = View.VISIBLE
    }

    private fun toastError(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}

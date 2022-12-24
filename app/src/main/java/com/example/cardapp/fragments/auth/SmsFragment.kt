package com.example.cardapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentSmsBinding
import com.example.cardapp.extensions.activityNavController
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.viewmodels.PhoneSmsFragmentViewModel
import com.example.cardapp.viewmodels.status.PhoneAuthStatus
import com.fraggjkee.smsconfirmationview.SmsConfirmationView


class SmsFragment : Fragment() {
    var _binding: FragmentSmsBinding? = null
    val binding
        get() = _binding!!
    private val viewModel: PhoneSmsFragmentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmsBinding.inflate(layoutInflater, container, false)
        setupCodeObserver()
        setupSmsView()
        setupPhoneAuthStatusObserver()
        return binding.root
    }


    private fun setupSmsView() {
        binding.smsConfirmationView.onChangeListener =
            SmsConfirmationView.OnChangeListener { code, isComplete ->
                if (isComplete) {
                    viewModel.loginWithPhone(code)
                }
            }
    }

    private fun setupCodeObserver() {
        viewModel.code.observe(viewLifecycleOwner) {
            binding.smsConfirmationView.enteredCode = it
        }
    }

    private fun setupPhoneAuthStatusObserver() {
        viewModel.authStatus.observe(viewLifecycleOwner) {
            when (it) {
                PhoneAuthStatus.CredentialsError -> toastError(getString(R.string.wrong_code))
                PhoneAuthStatus.InternetError -> toastError(getString(R.string.internet_error))
                PhoneAuthStatus.NotRegistered -> findNavController().navigateSafely(R.id.action_smsFragment_to_nameFragment)
                PhoneAuthStatus.Registered -> activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                PhoneAuthStatus.SuccessLogin -> {} //nothing
                PhoneAuthStatus.UnknownError -> toastError(getString(R.string.error))
            }
        }
    }

    private fun toastError(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}
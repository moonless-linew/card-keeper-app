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
import com.example.cardapp.databinding.FragmentPhoneBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.extensions.toPhoneStandard
import com.example.cardapp.viewmodels.status.SmsStatus
import com.example.cardapp.utils.PhoneUtils
import com.example.cardapp.viewmodels.PhoneSmsFragmentViewModel
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class PhoneFragment : Fragment() {
    private val viewModel: PhoneSmsFragmentViewModel by activityViewModels()
    private var _binding: FragmentPhoneBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(layoutInflater, container, false)
        toastError("Phone auth may work unpredictable")
        setupClickContinue()
        setupEditPhone()
        setupSmsStatusObserver()
        return binding.root
    }


    private fun setupClickContinue() {
        binding.phoneContinueButton.setOnClickListener {
            if (checkPhoneLength() == PhoneUtils.PHONE_LENGTH) {
                startLoading()
                viewModel.initPhoneAuth(
                    binding.editPhone.text.toString().toPhoneStandard(),
                    requireActivity()
                )
            } else {
                toastError(getString(R.string.phone_error))
            }
        }
    }

    private fun setupSmsStatusObserver() {
        viewModel.smsStatus.observe(viewLifecycleOwner) {
            when (it) {
                SmsStatus.CodeSent -> findNavController().navigateSafely(
                    R.id.action_phoneFragment_to_smsFragment,
                )
                SmsStatus.VerificationCompleted -> {}
                SmsStatus.VerificationFailed -> toastError(getString(R.string.error))
                SmsStatus.TooManyRequests -> toastError(getString(R.string.too_much_requests))
            }
            stopLoading()
        }
    }

    private fun setupEditPhone() {
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(binding.editPhone)
    }

    private fun startLoading() {
        binding.phoneProgressBar.visibility = View.VISIBLE
        binding.phoneContinueButton.visibility = View.INVISIBLE
    }

    private fun stopLoading() {
        binding.phoneProgressBar.visibility = View.INVISIBLE
        binding.phoneContinueButton.visibility = View.VISIBLE
    }

    private fun toastError(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun checkPhoneLength() = binding.editPhone.text?.length ?: 0

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

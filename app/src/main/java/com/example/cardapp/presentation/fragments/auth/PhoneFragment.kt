package com.example.cardapp.presentation.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentPhoneBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.extensions.toPhoneStandard
import com.example.cardapp.utils.PhoneUtils
import com.example.cardapp.presentation.viewmodels.PhoneSmsFragmentViewModel
import com.example.cardapp.presentation.model.status.SmsStatus
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

@AndroidEntryPoint
class PhoneFragment : Fragment(R.layout.fragment_phone) {
    private val viewModel: PhoneSmsFragmentViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPhoneBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toastError("Phone auth may work unpredictable")
        setupClickContinue()
        setupEditPhone()
        setupSmsStatusObserver()
        super.onViewCreated(view, savedInstanceState)
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
                is SmsStatus.Error -> toastError(getString(it.stringId))
                else -> {}
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

}

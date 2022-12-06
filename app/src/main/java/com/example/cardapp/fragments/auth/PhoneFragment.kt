package com.example.cardapp.fragments.auth

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentPhoneBinding
import com.example.cardapp.utils.PhoneUtils
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class PhoneFragment : Fragment() {
    private var _binding: FragmentPhoneBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(layoutInflater, container, false)
        setupClickContinue()
        setupEditPhone()
        return binding.root
    }


    private fun setupClickContinue() {
        binding.phoneContinueButton.setOnClickListener {
            if((binding.editPhone.text?.length ?: 0) == PhoneUtils.PHONE_LENGTH){
                findNavController().navigateSafely(R.id.action_phoneFragment_to_smsFragment)
            }
            else{
                Toast.makeText(requireActivity(), getString(R.string.phone_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditPhone() {
        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(binding.editPhone)
    }

    private fun NavController.navigateSafely(@IdRes actionId: Int) {
        currentDestination?.getAction(actionId)?.let { navigate(actionId) }
    }
}

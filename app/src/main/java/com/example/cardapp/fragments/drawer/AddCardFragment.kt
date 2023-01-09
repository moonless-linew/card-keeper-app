package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentAddCardBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.viewmodels.AddCardFragmentViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AddCardFragment: Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: AddCardFragmentViewModel by activityViewModels()
    private val barcodeScanLauncher = registerForActivityResult(ScanContract()) {
        binding.textEnterID.editText?.setText(it.contents)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)
        setupTextInputs()
        setupDoneButton()
        Toast.makeText(requireContext(), viewModel.chosenMarket?.name, Toast.LENGTH_SHORT).show()
        return binding.root
    }

    private fun setupTextInputs(){
        binding.textEnterID.setEndIconOnClickListener {
            barcodeScanLauncher.launch(ScanOptions().also {
                it.setOrientationLocked(true)
                it.setBarcodeImageEnabled(true)
                it.setPrompt(getString(R.string.scan_card))
            })
        }
    }
    private fun setupDoneButton(){
        binding.doneButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_addCardFragment_to_cardsFragment)
        }
    }

}
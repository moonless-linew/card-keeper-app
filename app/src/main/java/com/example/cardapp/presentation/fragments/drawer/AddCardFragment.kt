package com.example.cardapp.presentation.fragments.drawer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.databinding.FragmentAddCardBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.presentation.viewmodels.AddCardFragmentViewModel
import com.example.cardapp.presentation.model.status.CardUploadStatus
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardFragment : Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: AddCardFragmentViewModel by activityViewModels()
    private val barcodeScanLauncher = registerForActivityResult(ScanContract()) {
        binding.textEnterID.editText?.setText(it.contents)
        viewModel.setCardCodeType(it.formatName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)
        setupTextInputs()
        setupDoneButton()
        setupCardDataObserver()
        setupCardUploadObserver()
        binding.RelativeView.setBackgroundColor(Color.parseColor(viewModel.chosenCard.value?.marketNetwork?.color))
        return binding.root
    }

    private fun setupTextInputs() {
        binding.textEnterID.run {
            setEndIconOnClickListener {
                barcodeScanLauncher.launch(ScanOptions().also {
                    it.setOrientationLocked(true)
                    it.setBarcodeImageEnabled(true)
                    it.setPrompt(getString(R.string.scan_card))
                })
            }
            editText?.addTextChangedListener {
                viewModel.setCardID(it.toString())
            }

        }


    }

    private fun setupDoneButton() {
        binding.doneButton.setOnClickListener {
            if (binding.cardID.text.length < CardsUtils.MIN_ID_LENGTH)
                toastError(getString(R.string.small_id))
            else uploadCard()

        }
    }

    private fun uploadCard() {
        viewModel.uploadCard()
        startLoading()
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.doneButton.visibility = View.INVISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.doneButton.visibility = View.VISIBLE
    }


    private fun setupCardDataObserver() {
        viewModel.chosenCard.observe(viewLifecycleOwner) {
            binding.cardID.text = it.id
            binding.marketName.text = viewModel.chosenCard.value?.marketNetwork?.name
        }
    }

    private fun setupCardUploadObserver() {
        viewModel.cardUploadingStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardUploadStatus.Fail -> toastError(getString(R.string.error))
                CardUploadStatus.Success -> {
                    findNavController().navigateSafely(R.id.action_addCardFragment_to_cardsFragment)
                    viewModel.reset()
                    stopLoading()
                }
                CardUploadStatus.Null -> {} //nothing
            }
        }
    }

    private fun toastError(msg: String) =
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()


}

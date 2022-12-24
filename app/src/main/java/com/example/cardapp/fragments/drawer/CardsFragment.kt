package com.example.cardapp.fragments.drawer


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R

import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.databinding.SheetCardBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.interfaces.CardCallback
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.viewmodels.CardsFragmentViewModel
import com.example.cardapp.viewmodels.status.CardDataStatus

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CardsFragmentViewModel by viewModels()
    private val cardCallback: CardCallback = CardCallback {
        BottomSheetDialog(requireActivity()).also { dialog ->
            val dialogBinding: SheetCardBinding = SheetCardBinding.inflate(layoutInflater)
            dialogBinding.bottomSheetCardId.text = it.id
            dialogBinding.bottomSheetMarketName.text = it.market?.name
            dialogBinding.bottomSheetQrView.also { image ->
                image.setImageBitmap(generateQRCodeBitmap(it.id ?: CardsUtils.DEFAULT_ID,
                    BarcodeFormat.valueOf(it.codeType ?: CardsUtils.DEFAULT_FORMAT)))
            }
            dialog.setContentView(dialogBinding.root)
            dialog.show()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCardsBinding.inflate(layoutInflater, container, false)
        setupFloatingButton()
        setupObservers()
        downloadData()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.cardsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardDataStatus.Fail -> {}
                CardDataStatus.Success -> {
                    binding.cardsRecycler.adapter =
                        CardsRecyclerAdapter(viewModel.cardsData, cardCallback)
                    stopLoading()
                }
                CardDataStatus.Empty -> {
                    binding.cardsRecycler.visibility = View.INVISIBLE
                    binding.textNothingToShow.visibility = View.VISIBLE
                }
                CardDataStatus.Nan -> downloadData()
            }
        }
    }

    private fun setupFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigateSafely(R.id.action_cardsFragment_to_addCardFragment)
        }
    }

    private fun downloadData() {
        viewModel.downloadUserCards(Firebase.auth.uid!!)
        startLoading()

    }

    private fun startLoading() {
        binding.cardsRecycler.showShimmerAdapter()
    }

    private fun stopLoading() {
        binding.cardsRecycler.hideShimmerAdapter()
    }

    private fun generateQRCodeBitmap(id: String, format: BarcodeFormat): Bitmap {
        BarcodeEncoder().also {
            return it.encodeBitmap(id, format, 200, 200)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
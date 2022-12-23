package com.example.cardapp.fragments.drawer

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
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.interfaces.CardCallback
import com.example.cardapp.viewmodels.CardsFragmentViewModel
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CardsFragmentViewModel by viewModels()
    private val cardCallback: CardCallback = CardCallback {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheetCard.bottomSheet)
        binding.sheetCard.bottomSheetCardId.text = it.id
        binding.sheetCard.bottomSheetMarketName.text = it.market?.name
        bottomSheetBehavior.state =
            BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
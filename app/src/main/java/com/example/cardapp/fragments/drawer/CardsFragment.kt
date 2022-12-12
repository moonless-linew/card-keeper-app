package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.viewmodels.CardsFragmentViewModel
import com.example.cardapp.viewmodels.status.CardDataStatus

class CardsFragment: Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: CardsFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(layoutInflater, container, false)
        binding.cardsRecycler.adapter = CardsRecyclerAdapter(CardsUtils.TEST)
        binding.cardsRecycler.showShimmerAdapter()
        setupObservers()
        viewModel.downloadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers(){
        viewModel.cardsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardDataStatus.Fail -> {}
                CardDataStatus.Success -> binding.cardsRecycler.hideShimmerAdapter()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
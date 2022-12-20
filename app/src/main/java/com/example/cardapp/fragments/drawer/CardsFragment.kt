package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.viewmodels.CardsFragmentViewModel
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        binding.cardsRecycler.showShimmerAdapter()
        setupObservers()
        viewModel.downloadData(Firebase.auth.uid!!)
        return binding.root
    }

    private fun setupObservers(){
        viewModel.cardsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                CardDataStatus.Fail -> {}
                CardDataStatus.Success -> {
                    binding.cardsRecycler.adapter = CardsRecyclerAdapter(viewModel.cardsData)
                    binding.cardsRecycler.hideShimmerAdapter()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
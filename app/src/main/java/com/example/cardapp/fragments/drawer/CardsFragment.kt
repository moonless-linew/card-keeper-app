package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cardapp.adapters.CardsRecyclerAdapter
import com.example.cardapp.databinding.FragmentCardsBinding
import com.example.cardapp.utils.CardsUtils

class CardsFragment: Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(layoutInflater, container, false)
        binding.cardsRecycler.adapter = CardsRecyclerAdapter(CardsUtils.TEST)
        binding.cardsRecycler.showShimmerAdapter()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.cardsRecycler.hideShimmerAdapter()
        }, 5000)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
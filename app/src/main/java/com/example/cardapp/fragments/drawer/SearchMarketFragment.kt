package com.example.cardapp.fragments.drawer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cardapp.R
import com.example.cardapp.adapters.MarketsRecyclerAdapter
import com.example.cardapp.databinding.FragmentMarketSearchBinding
import com.example.cardapp.extensions.navigateSafely
import com.example.cardapp.models.MarketNetwork
import com.example.cardapp.viewmodels.AddCardFragmentViewModel
import com.example.cardapp.viewmodels.status.MarketDataStatus

class SearchMarketFragment : Fragment(R.layout.fragment_market_search) {

    private val binding by viewBinding(FragmentMarketSearchBinding::bind)
    private val viewModel: AddCardFragmentViewModel by activityViewModels()
    private val callback: (market: MarketNetwork) -> Unit = {
        viewModel.setMarket(it)
        findNavController().navigateSafely(R.id.action_searchMarketFragment_to_addCardFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupObservers() {
        viewModel.marketsDataStatus.observe(viewLifecycleOwner) {
            when (it) {
                MarketDataStatus.Fail -> toastError(getString(R.string.error))
                MarketDataStatus.Success -> applyMarkets()
                MarketDataStatus.Null -> downloadMarkets()
            }
        }
    }

    private fun applyMarkets() {
        binding.marketsRecycler.adapter = MarketsRecyclerAdapter(viewModel.marketsData, callback)
        stopLoading()
    }

    private fun downloadMarkets() {
        viewModel.downloadMarkets()
        startLoading()

    }

    private fun toastError(msg: String) =
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()


    private fun startLoading() = binding.marketsRecycler.showShimmerAdapter()

    private fun stopLoading() = binding.marketsRecycler.hideShimmerAdapter()


}
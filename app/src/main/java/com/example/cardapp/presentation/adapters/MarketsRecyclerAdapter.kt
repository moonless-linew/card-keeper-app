package com.example.cardapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cardapp.databinding.ItemMarketBinding
import com.example.cardapp.domain.model.MarketNetwork

class MarketsRecyclerAdapter(
    val marketNetworks: List<MarketNetwork>,
    val callback: (marketNetwork: MarketNetwork) -> Unit,
) : Adapter<MarketsRecyclerAdapter.MarketViewHolder>() {
    class MarketViewHolder(val binding: ItemMarketBinding) : ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(
            ItemMarketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {

        holder.binding.marketName.text = marketNetworks[position].name
        holder.binding.root.setOnClickListener {
            callback(marketNetworks[position])
        }
    }

    override fun getItemCount(): Int {
        return marketNetworks.size
    }
}

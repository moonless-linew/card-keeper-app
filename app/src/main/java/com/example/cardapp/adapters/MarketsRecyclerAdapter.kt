package com.example.cardapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cardapp.databinding.ItemMarketBinding
import com.example.cardapp.models.MarketNetwork

class MarketsRecyclerAdapter(val markets: List<MarketNetwork>, val callback: (market: MarketNetwork) -> Unit) :
    Adapter<MarketsRecyclerAdapter.MarketViewHolder>() {
    class MarketViewHolder(val binding: ItemMarketBinding) : ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(ItemMarketBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {

        holder.binding.marketName.text = markets[position].name
        holder.binding.root.setOnClickListener{
            callback(markets[position])
        }
    }

    override fun getItemCount(): Int {
        return markets.size
    }
}

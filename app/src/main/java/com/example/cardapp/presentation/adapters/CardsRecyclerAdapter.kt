package com.example.cardapp.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cardapp.databinding.ItemCardBinding
import com.example.cardapp.domain.model.Card

class CardsRecyclerAdapter(val data: List<Card>, val callback: CardCallback) : Adapter<CardsRecyclerAdapter.ItemViewHolder>() {


    class ItemViewHolder(val binding: ItemCardBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.cardID.text = data[position].id
        holder.binding.marketName.text = data[position].market?.name
        holder.binding.RelativeView.setBackgroundColor(Color.parseColor(data[position].market?.color))

        holder.binding.root.setOnClickListener {
            callback.onClick(data[position])

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

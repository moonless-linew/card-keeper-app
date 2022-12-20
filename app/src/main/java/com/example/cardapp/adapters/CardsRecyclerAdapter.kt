package com.example.cardapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cardapp.databinding.ItemCardBinding
import com.example.cardapp.models.Card

class CardsRecyclerAdapter(val data: List<Card>) : Adapter<CardsRecyclerAdapter.ItemViewHolder>() {


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
        holder.binding.descriptionTextView.text = data[position].id
        //todo
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
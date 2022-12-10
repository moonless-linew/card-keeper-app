package com.example.cardapp.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cardapp.R
import com.example.cardapp.databinding.ItemCardBinding
import com.example.cardapp.models.Card
import com.example.cardapp.utils.SlidesUtils

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
        holder.binding.marketNameTextView.text = data[position].market
        holder.binding.descriptionTextView.text = data[position].description
        holder.binding.iconCard.setImageResource(data[position].image)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
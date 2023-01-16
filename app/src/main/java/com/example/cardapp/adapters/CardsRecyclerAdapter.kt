package com.example.cardapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cardapp.databinding.ItemCardBinding
import com.example.cardapp.interfaces.CardCallback
import com.example.cardapp.models.Card
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

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
        holder.binding.root.setOnClickListener { callback.onClick(data[position]) }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
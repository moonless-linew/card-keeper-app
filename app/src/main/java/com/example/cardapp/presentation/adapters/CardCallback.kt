package com.example.cardapp.presentation.adapters

import com.example.cardapp.domain.model.Card

interface CardCallback{
    fun onClick(card: Card)
}

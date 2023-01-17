package com.example.cardapp.interfaces

import com.example.cardapp.models.Card

interface CardCallback{
    fun onClick(card: Card)
}
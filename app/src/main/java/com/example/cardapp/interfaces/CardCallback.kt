package com.example.cardapp.interfaces

import com.example.cardapp.models.Card

fun interface CardCallback {
    fun onClick(card: Card)
}
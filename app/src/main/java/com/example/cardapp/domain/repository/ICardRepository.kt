package com.example.cardapp.domain.repository

import com.example.cardapp.models.Card

interface ICardRepository {
    suspend fun uploadCard(uid: String, card: Card)
    suspend fun deleteCard(uid: String, card: Card)
    suspend fun getUserCards(uid: String): List<Card>
}

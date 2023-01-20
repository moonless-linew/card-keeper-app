package com.example.cardapp.presentation.model.status

import com.example.cardapp.models.Card

sealed interface CardDataStatus{
    class Success(val cards: List<Card>): CardDataStatus
    object Fail: CardDataStatus
    object Empty: CardDataStatus
    object Null: CardDataStatus
}

package com.example.cardapp.presentation.model.status

sealed class CardDataStatus{
    object Success: CardDataStatus()
    object Fail: CardDataStatus()
    object Empty: CardDataStatus()
    object Null: CardDataStatus()
}

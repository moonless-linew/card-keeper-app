package com.example.cardapp.viewmodels.status

sealed class CardDataStatus{
    object Success: CardDataStatus()
    object Fail: CardDataStatus()
}

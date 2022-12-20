package com.example.cardapp.viewmodels.status

import com.example.cardapp.models.Card

sealed class CardDataStatus{
    object Success: CardDataStatus()
    object Fail: CardDataStatus()
    object Empty: CardDataStatus()
    object Nan: CardDataStatus()
}

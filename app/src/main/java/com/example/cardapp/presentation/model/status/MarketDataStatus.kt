package com.example.cardapp.presentation.model.status

sealed class MarketDataStatus {
    object Success: MarketDataStatus()
    object Fail: MarketDataStatus()
    object Null: MarketDataStatus()
}

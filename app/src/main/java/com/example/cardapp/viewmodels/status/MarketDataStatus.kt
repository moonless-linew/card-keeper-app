package com.example.cardapp.viewmodels.status

sealed class MarketDataStatus {
    object Success: MarketDataStatus()
    object Fail: MarketDataStatus()
    object Null: MarketDataStatus()
}
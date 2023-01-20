package com.example.cardapp.presentation.model.status

import com.example.cardapp.domain.model.Market

sealed interface MarketDataStatus {
    class Success(val markets: List<Market>): MarketDataStatus
    object Fail: MarketDataStatus
    object Null: MarketDataStatus
}

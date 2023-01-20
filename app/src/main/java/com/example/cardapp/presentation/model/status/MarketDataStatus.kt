package com.example.cardapp.presentation.model.status

import com.example.cardapp.domain.model.MarketNetwork

sealed interface MarketDataStatus {
    class Success(val marketNetworks: List<MarketNetwork>): MarketDataStatus
    object Fail: MarketDataStatus
    object Null: MarketDataStatus
}

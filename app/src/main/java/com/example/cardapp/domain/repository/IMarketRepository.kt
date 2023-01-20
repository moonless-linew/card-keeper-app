package com.example.cardapp.domain.repository

import com.example.cardapp.domain.model.MarketAndDistance
import com.example.cardapp.domain.model.MarketNetwork

interface IMarketRepository {
    suspend fun getMarketsByIds(ids: List<String>): List<MarketNetwork>
    suspend fun getAllMarkets(): List<MarketNetwork>

    suspend fun getMinDistanceMarkets(
        marketNetIds: List<String>,
        latitude: Double,
        longitude: Double,
    ): List<MarketAndDistance>
}

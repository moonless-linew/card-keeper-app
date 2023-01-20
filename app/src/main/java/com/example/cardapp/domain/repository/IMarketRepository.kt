package com.example.cardapp.domain.repository

import com.example.cardapp.domain.model.Market

interface IMarketRepository {
    suspend fun getMarketsByIds(ids: List<String>): List<Market>
    suspend fun getAllMarkets(): List<Market>
}

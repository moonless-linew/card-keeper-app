package com.example.cardapp.data.repository

import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.Market
import com.example.cardapp.utils.ApiUtils
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MarketRepository @Inject constructor(
    @Named("markets")
    private val marketsRef: CollectionReference,
) : IMarketRepository {
    override suspend fun getMarketsByIds(
        ids: List<String>,
    ): List<Market> = suspendCoroutine { continuation ->
        marketsRef
            .whereIn(ApiUtils.API_ID_FIELD, ids)
            .get()
            .addOnSuccessListener { documents ->
                val markets = documents.toObjects(Market::class.java)
                continuation.resume(markets)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getAllMarkets(): List<Market> = suspendCoroutine { continuation ->
        marketsRef
            .get()
            .addOnSuccessListener { documents ->
                val markets = documents.toObjects(Market::class.java)
                continuation.resume(markets)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }
}
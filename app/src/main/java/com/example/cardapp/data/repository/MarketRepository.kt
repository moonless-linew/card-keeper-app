package com.example.cardapp.data.repository

import com.example.cardapp.domain.model.MarketAndDistance
import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.MarketNetwork
import com.example.cardapp.utils.ApiUtils
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.fonfon.kgeohash.GeoHash
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MarketRepository @Inject constructor(
    @Named("markets")
    private val marketNetsRef: CollectionReference,
) : IMarketRepository {
    override suspend fun getMarketsByIds(
        ids: List<String>,
    ): List<MarketNetwork> = suspendCoroutine { continuation ->
        marketNetsRef
            .whereIn(ApiUtils.API_ID_FIELD, ids)
            .get()
            .addOnSuccessListener { documents ->
                val marketNetworks = documents.toObjects(MarketNetwork::class.java)
                continuation.resume(marketNetworks)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getAllMarkets(): List<MarketNetwork> = suspendCoroutine { continuation ->
        marketNetsRef
            .get()
            .addOnSuccessListener { documents ->
                val marketNetworks = documents.toObjects(MarketNetwork::class.java)
                continuation.resume(marketNetworks)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getMinDistanceMarkets(
        marketNetIds: List<String>,
        latitude: Double,
        longitude: Double,
    ): List<MarketAndDistance> {
        val geoHash = GeoHash(latitude, longitude, 7)
        val neighbours = geoHash.adjacentBox.map { it.toString() }

        val marketsAndDistance = suspendCoroutine {  continuation ->
            FirebaseFirestore.getInstance().collection("market2")
                .whereIn("geohash7", neighbours)
                .get()
                .addOnSuccessListener {
                    continuation.resume(it.documents.map { doc ->
                        val geolocation = GeoLocation(latitude, longitude)
                        val netId = doc.get("NetId").toString()
                        val geoPoint = doc.get("geopoint") as HashMap<String, Double>
                        val geoLoc = GeoLocation(geoPoint["latitude"]!!, geoPoint["longgitude"]!!)
                        val distanceInM = GeoFireUtils.getDistanceBetween(geoLoc, geolocation)
                        return@map MarketAndDistance(netId, distanceInM)
                    })
                }
        }
        val sortedByDistance =
            marketsAndDistance.filter { it.netId in marketNetIds }.sortedBy { it.distance }
        val minDistanceMarkets = sortedByDistance.filterIndexed { index, current ->
            index == sortedByDistance.indexOfFirst { current.netId == it.netId }
        }
        return minDistanceMarkets
    }
}

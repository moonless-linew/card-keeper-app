package com.example.cardapp.presentation.viewmodels

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.datasource.local.location.provideLastLocation
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.Card
import com.example.cardapp.domain.model.MarketAndDistance
import com.example.cardapp.domain.model.MarketNetwork
import com.example.cardapp.presentation.model.status.CardDataStatus
import com.example.cardapp.presentation.model.status.CompletableTaskStatus
import com.example.cardapp.utils.CardsUtils
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.fonfon.kgeohash.GeoHash
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CardsFragmentViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    private val marketRepository: IMarketRepository,
) : ViewModel() {
    private var _cardsDataStatus =
        MutableLiveData<CardDataStatus>().also { it.value = CardDataStatus.Null }
    val cardsDataStatus: LiveData<CardDataStatus>
        get() = _cardsDataStatus

    private var _deleteCardStatus = MutableLiveData<CompletableTaskStatus>()
    val deleteCardStatus: LiveData<CompletableTaskStatus>
        get() = _deleteCardStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    }

    fun getUsersCards(uid: String, locationClient: FusedLocationProviderClient?) {
        viewModelScope.launch {
            try {
                val cardsData = cardRepository.getUserCards(uid)
                if (cardsData.isEmpty()) {
                    _cardsDataStatus.postValue(CardDataStatus.Empty)
                } else {
                    val marketsId = cardsData.mapNotNull { it.marketID }.toSet().toList()
                    val nearestMarkets = getNearestMarkets(marketsId, locationClient)
                    getUserMarkets(cardsData, nearestMarkets)
                }
            } catch (e: Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getNearestMarkets(
        marketNetIds: List<String>,
        locationClient: FusedLocationProviderClient? = null,
    ): List<MarketAndDistance> {
        val currentLocation = locationClient?.provideLastLocation() ?: return emptyList()

        return marketRepository.getMinDistanceMarkets(
            marketNetIds,
            currentLocation.latitude,
            currentLocation.longitude
        )
    }

    private suspend fun getUserMarkets(
        cards: List<Card>,
        minDistanceMarkets: List<MarketAndDistance>,
    ) {
        val ids = cards.map { it.marketID ?: "" }.toSet()
        val markets = marketRepository.getMarketsByIds(ids.toList())
        for (i in cards.indices) {
            cards[i].marketNetwork = markets.find { it.id == cards[i].marketID }
        }
        val sortedCards = if (minDistanceMarkets.isNotEmpty())
            cards.sortedWith(compareBy<Card> { card ->
                minDistanceMarkets.find { it.netId == card.marketID }?.distance
                    ?: Double.MAX_VALUE
            }.thenBy { it.marketNetwork?.name ?: ""} ) else cards
        _cardsDataStatus.postValue(CardDataStatus.Success(sortedCards))
    }

    fun generateQRCodeBitmap(id: String, format: BarcodeFormat): Bitmap {
        BarcodeEncoder().also { it1 ->
            return it1.encodeBitmap(id, format, CardsUtils.QR_CODE_WIDTH, CardsUtils.QR_CODE_HEIGHT)
        }
    }

    fun deleteCard(uid: String, card: Card) {
        viewModelScope.launch(exceptionHandler) {
            val taskStatus = try {
                cardRepository.deleteCard(uid, card)
                CompletableTaskStatus.Success
            } catch (e: Exception) {
                CompletableTaskStatus.Fail
            }
            _deleteCardStatus.value = taskStatus
        }
    }
}

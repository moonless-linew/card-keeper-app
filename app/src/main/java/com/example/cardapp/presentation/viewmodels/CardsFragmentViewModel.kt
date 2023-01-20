package com.example.cardapp.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.Card
import com.example.cardapp.domain.model.Market
import com.example.cardapp.presentation.model.status.CardDataStatus
import com.example.cardapp.presentation.model.status.CompletableTaskStatus
import com.example.cardapp.utils.CardsUtils
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsFragmentViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    private val marketRepository: IMarketRepository
) : ViewModel() {
    private var _cardsDataStatus = MutableLiveData<CardDataStatus>().also { it.value = CardDataStatus.Null }
    val cardsDataStatus: LiveData<CardDataStatus>
        get() = _cardsDataStatus

    private var _deleteCardStatus = MutableLiveData<CompletableTaskStatus>()
    val deleteCardStatus: LiveData<CompletableTaskStatus>
        get() = _deleteCardStatus

    lateinit var marketsData: List<Market>

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    }

    fun downloadUserCards(uid: String) {
        viewModelScope.launch {
            val cardsData = cardRepository.getUserCards(uid)
            if (cardsData.isEmpty()) {
                _cardsDataStatus.postValue(CardDataStatus.Empty)
            } else {
                downloadUserMarkets(cardsData)
            }
        }
    }

    private fun downloadUserMarkets(cards: List<Card>) {
        val ids = mutableSetOf<String>().also { it1 ->
            cards.forEach {
                it1.add(it.marketID.toString())
            }
        }
        viewModelScope.launch {
            try {
                val markets = marketRepository.getMarketsByIds(ids.toList())
                for (i in cards.indices) {
                    cards[i].market = markets.find { it.id == cards[i].marketID }
                }
                _cardsDataStatus.postValue(CardDataStatus.Success(cards))
            } catch (e: Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }
        }
    }

    fun generateQRCodeBitmap(id: String, format: BarcodeFormat): Bitmap {
        BarcodeEncoder().also { it1 ->
            return it1.encodeBitmap(id, format, CardsUtils.QR_CODE_WIDTH, CardsUtils.QR_CODE_HEIGHT)
        }
    }
    fun deleteCard(uid: String, card: Card){
        viewModelScope.launch {
            cardRepository.deleteCard(uid, card)
        }
    }
}

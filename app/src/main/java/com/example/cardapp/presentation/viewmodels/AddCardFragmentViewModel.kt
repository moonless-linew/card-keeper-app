package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.Card
import com.example.cardapp.domain.model.Market
import com.example.cardapp.presentation.model.status.CardUploadStatus
import com.example.cardapp.presentation.model.status.MarketDataStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardFragmentViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    private val marketRepository: IMarketRepository,
) : ViewModel() {
    private var _marketsDataStatus =
        MutableLiveData<MarketDataStatus>().also { it.value = MarketDataStatus.Null }
    val marketsDataStatus: LiveData<MarketDataStatus>
        get() = _marketsDataStatus

    var _chosenCard = MutableLiveData<Card>()
    val chosenCard: LiveData<Card>
        get() = _chosenCard

    var _cardUploadingStatus = MutableLiveData<CardUploadStatus>()
    val cardUploadingStatus: LiveData<CardUploadStatus>
        get() = _cardUploadingStatus

    fun downloadMarkets() {
        viewModelScope.launch {
            try {
                val markets = marketRepository.getAllMarkets()
                _marketsDataStatus.postValue(MarketDataStatus.Success(markets))
            } catch (e: Exception) {
                _marketsDataStatus.postValue(MarketDataStatus.Fail)
            }

        }
    }

    fun uploadCard() {
        val uid = Firebase.auth.uid ?: return
        viewModelScope.launch {
            cardRepository.uploadCard(uid, _chosenCard.value ?: Card())
        }
        _cardUploadingStatus.postValue(CardUploadStatus.Success)
    }

    fun setMarket(market: Market) {
        _chosenCard.postValue(Card().also {
            it.market = market
            it.marketID = market.id
        })
    }

    fun setCardCodeType(codeType: String?) {
        _chosenCard.value?.codeType = codeType
        _chosenCard.postValue(_chosenCard.value)
    }

    fun setCardID(id: String) {
        _chosenCard.value?.id = id
        _chosenCard.postValue(_chosenCard.value)
    }

    fun reset() {
        _chosenCard.value = Card()
        _cardUploadingStatus.value = CardUploadStatus.Null
    }
}

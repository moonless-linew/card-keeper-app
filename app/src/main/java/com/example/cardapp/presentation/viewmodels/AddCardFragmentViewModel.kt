package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.domain.repository.IMarketRepository
import com.example.cardapp.domain.model.Card
import com.example.cardapp.domain.model.MarketNetwork
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

    fun downloadMarkets(marketNetIds: Array<String>) {
        viewModelScope.launch {
            try {
                val markets = marketRepository.getAllMarkets().filter { it.id !in marketNetIds }
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

    fun setMarket(marketNetwork: MarketNetwork) {
        _chosenCard.postValue(
            Card(marketNetwork = marketNetwork, marketID = marketNetwork.id)
        )
    }

    fun setCardCodeType(codeType: String?) {
        val changedCard = _chosenCard.value?.copy(codeType = codeType) ?: return
        _chosenCard.postValue(changedCard)
    }

    fun setCardID(id: String) {
        val changedCard = _chosenCard.value?.copy(id = id) ?: return
        _chosenCard.postValue(changedCard)
    }

    fun reset() {
        _chosenCard.value = Card()
        _cardUploadingStatus.value = CardUploadStatus.Null
    }
}

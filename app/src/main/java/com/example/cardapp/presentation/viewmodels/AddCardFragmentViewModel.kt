package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.data.repository.CardRepository
import com.example.cardapp.database.DataBase
import com.example.cardapp.domain.repository.ICardRepository
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.models.Market
import com.example.cardapp.presentation.model.status.CardUploadStatus
import com.example.cardapp.presentation.model.status.MarketDataStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardFragmentViewModel @Inject constructor(
    private val cardRepository: ICardRepository
): ViewModel() {


    lateinit var marketsData: List<Market>

    var _marketsDataStatus = MutableLiveData<MarketDataStatus>().also { it.value = MarketDataStatus.Null }
    val marketsDataStatus: LiveData<MarketDataStatus>
    get() = _marketsDataStatus

    var _chosenCard = MutableLiveData<Card>()
    val chosenCard: LiveData<Card>
        get() = _chosenCard

    var _cardUploadingStatus = MutableLiveData<CardUploadStatus>()
    val cardUploadingStatus: LiveData<CardUploadStatus>
        get() = _cardUploadingStatus

    fun downloadMarkets() {
        DataBase.downloadMarkets(object: OnCollectionDownloadCompleteListener{
            override fun onSuccess(documents: QuerySnapshot) {
                marketsData = documents.toObjects(Market::class.java)
                _marketsDataStatus.postValue(MarketDataStatus.Success)
            }

            override fun onFail(e: Exception) {
               _marketsDataStatus.postValue(MarketDataStatus.Fail)
            }

        })
    }

    fun uploadCard(){
        val uid = Firebase.auth.uid ?: return
        viewModelScope.launch {
            cardRepository.uploadCard(uid, _chosenCard.value ?: Card())
        }
        _cardUploadingStatus.postValue(CardUploadStatus.Success)
    }

    fun setMarket(market: Market){
        _chosenCard.postValue(Card().also{
            it.market = market
            it.marketID = market.id
        })
    }
    fun setCardCodeType(codeType: String?){
        _chosenCard.value?.codeType = codeType
        _chosenCard.postValue(_chosenCard.value)
    }
    fun setCardID(id: String){
        _chosenCard.value?.id = id
        _chosenCard.postValue(_chosenCard.value)
    }

    fun reset() {
        _chosenCard.value = Card()
        _cardUploadingStatus.value = CardUploadStatus.Null
    }


}

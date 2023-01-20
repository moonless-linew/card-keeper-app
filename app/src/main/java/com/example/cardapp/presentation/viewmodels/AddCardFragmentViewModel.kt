package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
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
import javax.inject.Inject

@HiltViewModel
class AddCardFragmentViewModel @Inject constructor(): ViewModel() {


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
        DataBase.uploadCard(Firebase.auth.uid!!, _chosenCard.value ?: Card(), object: OnCompleteListener{
            override fun onSuccess() {
                _cardUploadingStatus.postValue(CardUploadStatus.Success)
            }

            override fun onFail() {
                _cardUploadingStatus.postValue(CardUploadStatus.Fail)
            }

        })
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

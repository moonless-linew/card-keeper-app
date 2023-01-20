package com.example.cardapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.models.MarketNetwork
import com.example.cardapp.viewmodels.status.CardUploadStatus
import com.example.cardapp.viewmodels.status.MarketDataStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class AddCardFragmentViewModel: ViewModel() {


    lateinit var marketsData: List<MarketNetwork>

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
                marketsData = documents.toObjects(MarketNetwork::class.java)
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

    fun setMarket(marketNetwork: MarketNetwork){
        _chosenCard.postValue(Card().also{
            it.market = marketNetwork
            it.marketID = marketNetwork.id
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
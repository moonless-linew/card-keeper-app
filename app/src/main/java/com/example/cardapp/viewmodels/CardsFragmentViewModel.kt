package com.example.cardapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.models.Market
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.google.firebase.firestore.QuerySnapshot


class CardsFragmentViewModel : ViewModel() {
    private var _cardsDataStatus = MutableLiveData<CardDataStatus>()
    val cardsDataStatus: LiveData<CardDataStatus>
        get() = _cardsDataStatus

    lateinit var marketsData: List<Market>
    lateinit var cardsData: List<Card>


    fun downloadUserCards(uid: String) {
        DataBase.downloadUserCards(uid, object : OnCollectionDownloadCompleteListener {
            override fun onSuccess(documents: QuerySnapshot) {
                cardsData = documents.toObjects(Card::class.java)
                if (cardsData.isEmpty()) {
                    _cardsDataStatus.postValue(CardDataStatus.Empty)
                } else {
                    downloadUserMarkets()
                }

            }

            override fun onFail(e: Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }

        })
    }

    private fun downloadUserMarkets() {
        val ids = mutableSetOf<String>().also { it1 ->
            cardsData.forEach {
                it1.add(it.marketID.toString())
            }
        }
        DataBase.downloadUserMarkets(ids.toList(), object : OnCollectionDownloadCompleteListener{
            override fun onSuccess(documents: QuerySnapshot) {
                marketsData = documents.toObjects(Market::class.java)
                for ( i in cardsData.indices){
                    cardsData[i].market = marketsData.find { it.id == cardsData[i].marketID }
                }
                _cardsDataStatus.postValue(CardDataStatus.Success)
            }

            override fun onFail(e: java.lang.Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }

        })
    }
}
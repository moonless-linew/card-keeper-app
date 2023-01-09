package com.example.cardapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.models.Market
import com.example.cardapp.viewmodels.status.MarketDataStatus
import com.google.firebase.firestore.QuerySnapshot
import java.lang.Exception

class AddCardFragmentViewModel: ViewModel() {


    lateinit var marketsData: List<Market>

    var _marketsDataStatus = MutableLiveData<MarketDataStatus>().also { it.value = MarketDataStatus.Null }
    val marketsDataStatus: LiveData<MarketDataStatus>
    get() = _marketsDataStatus

    var chosenMarket: Market? = null

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

}
package com.example.cardapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.models.Market
import com.example.cardapp.viewmodels.status.MarketDataStatus

class AddCardFragmentViewModel: ViewModel() {

    var markets: List<Market>? = null

    var _marketsStatus = MutableLiveData<MarketDataStatus>()
    val marketsStatus: LiveData<MarketDataStatus>
    get() = _marketsStatus


}
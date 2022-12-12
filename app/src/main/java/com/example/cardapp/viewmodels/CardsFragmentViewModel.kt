package com.example.cardapp.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.models.Card
import com.example.cardapp.viewmodels.status.CardDataStatus

class CardsFragmentViewModel: ViewModel() {
    private var _cardsDataStatus = MutableLiveData<CardDataStatus>()
    val cardsDataStatus: LiveData<CardDataStatus>
    get() = _cardsDataStatus

    private var _cardsData = MutableLiveData<Card>()
    val cardsData: LiveData<Card>
    get() = _cardsData

    fun downloadData(){
        Handler(Looper.getMainLooper()).postDelayed({
            _cardsDataStatus.postValue(CardDataStatus.Success)
        }, 3000)
    }
}
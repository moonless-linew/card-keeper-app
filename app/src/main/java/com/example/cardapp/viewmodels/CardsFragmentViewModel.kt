package com.example.cardapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.google.firebase.firestore.QuerySnapshot

class CardsFragmentViewModel: ViewModel() {
    private var _cardsDataStatus = MutableLiveData<CardDataStatus>()
    val cardsDataStatus: LiveData<CardDataStatus>
    get() = _cardsDataStatus

    var cardsData: List<Card> = emptyList()


    fun downloadData(uid: String){
        DataBase.downloadUserCards(uid, object: OnCollectionDownloadCompleteListener{
            override fun onSuccess(documents: QuerySnapshot) {
                cardsData = documents.toObjects(Card::class.java)
                if (cardsData.isEmpty()){
                    _cardsDataStatus.postValue(CardDataStatus.Empty)
                } else{
                    _cardsDataStatus.postValue(CardDataStatus.Success)
                }

            }

            override fun onFail(e: Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }

        })
    }
}
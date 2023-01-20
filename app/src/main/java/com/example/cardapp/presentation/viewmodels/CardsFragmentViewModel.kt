package com.example.cardapp.presentation.viewmodels

import android.graphics.Bitmap
import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.models.Market
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.presentation.model.status.CardDataStatus
import com.example.cardapp.presentation.model.status.CompletableTaskStatus
import com.google.firebase.firestore.QuerySnapshot
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardsFragmentViewModel @Inject constructor() : ViewModel() {
    private var _cardsDataStatus = MutableLiveData<CardDataStatus>().also { it.value = CardDataStatus.Null }
    val cardsDataStatus: LiveData<CardDataStatus>
        get() = _cardsDataStatus

    private var _deleteCardStatus = MutableLiveData<CompletableTaskStatus>()
    val deleteCardStatus: LiveData<CompletableTaskStatus>
        get() = _deleteCardStatus

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
        DataBase.downloadMarketsWithIds(ids.toList(),
            object : OnCollectionDownloadCompleteListener {
                override fun onSuccess(documents: QuerySnapshot) {
                    marketsData = documents.toObjects(Market::class.java)
                    for (i in cardsData.indices) {
                        cardsData[i].market = marketsData.find { it.id == cardsData[i].marketID }
                    }
                    _cardsDataStatus.postValue(CardDataStatus.Success)
                }

                override fun onFail(e: java.lang.Exception) {
                    _cardsDataStatus.postValue(CardDataStatus.Fail)
                }

            })
    }
    fun generateQRCodeBitmap(id: String, format: BarcodeFormat): Bitmap {
        BarcodeEncoder().also { it1 ->
            return it1.encodeBitmap(id, format, CardsUtils.QR_CODE_WIDTH, CardsUtils.QR_CODE_HEIGHT)
        }
    }
    fun deleteCard(uid: String, card: Card){
        DataBase.deleteCard(uid, card, object: OnCompleteListener{
            override fun onSuccess() {
                _deleteCardStatus.postValue(CompletableTaskStatus.Success)
            }

            override fun onFail() {
                _deleteCardStatus.postValue(CompletableTaskStatus.Fail)
            }

        })
    }
}

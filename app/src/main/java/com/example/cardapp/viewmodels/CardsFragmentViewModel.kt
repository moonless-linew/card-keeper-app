package com.example.cardapp.viewmodels

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.models.Card
import com.example.cardapp.models.Market
import com.example.cardapp.models.MarketNetwork
import com.example.cardapp.utils.CardsUtils
import com.example.cardapp.viewmodels.status.CardDataStatus
import com.example.cardapp.viewmodels.status.CompletableTaskStatus
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryBounds
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class CardsFragmentViewModel : ViewModel() {
    private var _cardsDataStatus =
        MutableLiveData<CardDataStatus>().also { it.value = CardDataStatus.Null }
    val cardsDataStatus: LiveData<CardDataStatus>
        get() = _cardsDataStatus

    private var _deleteCardStatus = MutableLiveData<CompletableTaskStatus>()
    val deleteCardStatus: LiveData<CompletableTaskStatus>
        get() = _deleteCardStatus

    lateinit var marketsNetworkData: List<MarketNetwork>
    lateinit var cardsData: List<Card>
    lateinit var nearestMarkets: List<Market>

    var ids: MutableSet<String> = emptySet<String>().toMutableSet()

    private var userLocation: Location? = null


    val geoFire = GeoFireUtils.getDistanceBetween(GeoLocation(userLocation?.latitude ?: 0.0,
        userLocation?.altitude ?: 0.0),
        GeoLocation(2.0, 3.0))

    fun downloadUserCards(uid: String) {
        DataBase.downloadUserCards(uid, object : OnCollectionDownloadCompleteListener {
            override fun onSuccess(documents: QuerySnapshot) {
                cardsData = documents.toObjects(Card::class.java)

                ids.also { it1 ->
                    cardsData.forEach {
                        it1.add(it.marketID.toString())
                    }
                }

                if (cardsData.isEmpty()) {
                    _cardsDataStatus.postValue(CardDataStatus.Empty)
                } else {
                    if (userLocation?.latitude != null && userLocation?.longitude != null) {
                        downloadNearestMarkets()
                    }
                    downloadUserMarkets()
                }
            }

            override fun onFail(e: Exception) {
                _cardsDataStatus.postValue(CardDataStatus.Fail)
            }

        })
    }

    private fun downloadUserMarkets() {
        DataBase.downloadMarketsWithIds(ids.toList(),
            object : OnCollectionDownloadCompleteListener {
                override fun onSuccess(documents: QuerySnapshot) {
                    marketsNetworkData = documents.toObjects(MarketNetwork::class.java)
                    for (i in cardsData.indices) {
                        cardsData[i].market =
                            marketsNetworkData.find { it.id == cardsData[i].marketID }
                    }
                    _cardsDataStatus.postValue(CardDataStatus.Success)
                }

                override fun onFail(e: java.lang.Exception) {
                    _cardsDataStatus.postValue(CardDataStatus.Fail)
                }

            })
    }

    private fun downloadNearestMarkets() {
        val geoLocation = GeoLocation(userLocation?.latitude ?: 0.0, userLocation?.longitude ?: 0.0)

        val bounds = GeoFireUtils.getGeoHashQueryBounds(geoLocation, 70.0)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q = FirebaseFirestore.getInstance().collection("markets1")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }
        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val matchingDocs: MutableList<DocumentSnapshot> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val lat = doc.getDouble("lat")!!
                        val lng = doc.getDouble("long")!!


                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, geoLocation)
                        if (distanceInM <= 70.0) {
                            matchingDocs.add(doc)
                        }
                    }
                }

                val a = 0
            }
    }

    fun generateQRCodeBitmap(id: String, format: BarcodeFormat): Bitmap {
        BarcodeEncoder().also { it1 ->
            return it1.encodeBitmap(id, format, CardsUtils.QR_CODE_WIDTH, CardsUtils.QR_CODE_HEIGHT)
        }
    }

    fun deleteCard(uid: String, card: Card) {
        DataBase.deleteCard(uid, card, object : OnCompleteListener {
            override fun onSuccess() {
                _deleteCardStatus.postValue(CompletableTaskStatus.Success)
            }

            override fun onFail() {
                _deleteCardStatus.postValue(CompletableTaskStatus.Fail)
            }

        })
    }

    fun setCurrentLocation(location: Location?) {
        this.userLocation = location
    }
}
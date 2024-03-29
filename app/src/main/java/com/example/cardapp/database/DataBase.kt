package com.example.cardapp.database

import androidx.fragment.app.FragmentActivity
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener

import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.example.cardapp.models.Card

import com.example.cardapp.utils.ApiUtils
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Api
import java.util.concurrent.TimeUnit

object DataBase {

    fun loginUserWithName(listener: OnCompleteListener) {
        Firebase.auth.signInAnonymously().addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener {
            listener.onFail()
        }
    }

    fun loginUserWithPhone(
        credential: PhoneAuthCredential,
        listener: OnCompleteListener,
    ) {
        Firebase.auth.signInWithCredential(credential).addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener {
            listener.onFail()
        }
    }

    fun createUser(
        uid: String,
        name: String?,
        phone: String?,
        listener: OnCompleteListener,
    ) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .set(hashMapOf(ApiUtils.API_NAME_FIELD to name, ApiUtils.API_PHONE_FIELD to phone))
            .addOnSuccessListener {
                listener.onSuccess()
            }
            .addOnFailureListener {
                listener.onFail()
            }
    }

    fun downloadUser(uid: String, listener: OnDocumentDownloadCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnSuccessListener {
                listener.onSuccess(it)
            }
            .addOnFailureListener {
                listener.onFail(it)
            }
    }

    fun downloadListNearestMarket(netIds: List<String>, hashIds: List<String>, listener: OnCollectionDownloadCompleteListener){
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_MARKET_NETWORK_COLLECTION)
            .whereIn("NetId", netIds)
            .whereIn("geohash", hashIds)
            .get()
            .addOnSuccessListener { listener.onSuccess(it) }
            .addOnFailureListener{ listener.onFail(it) }
    }

    fun uploadCard(uid: String, card: Card, listener: OnCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .add(card)
            .addOnSuccessListener { listener.onSuccess() }
            .addOnFailureListener { listener.onFail() }
    }

    fun deleteCard(uid: String, card: Card, listener: OnCompleteListener) {
        val batch = FirebaseFirestore.getInstance().batch()
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .whereEqualTo(ApiUtils.API_ID_FIELD, card.id.toString())
            .get()
            .addOnSuccessListener { it1 ->
                it1.forEach {
                    batch.delete(it.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        listener.onSuccess()
                    }
                    .addOnFailureListener() {
                        listener.onFail()
                    }
            }
    }

    fun downloadUserCards(uid: String, listener: OnCollectionDownloadCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .collection(ApiUtils.API_CARDS_COLLECTION)
            .get()
            .addOnSuccessListener {
                listener.onSuccess(it)
            }
            .addOnFailureListener {
                listener.onFail(it)
            }
    }

    fun downloadMarketsWithIds(ids: List<String>, listener: OnCollectionDownloadCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_MARKETS_COLLECTION)
            .whereIn(ApiUtils.API_ID_FIELD, ids)
            .get()
            .addOnSuccessListener {
                listener.onSuccess(it)
            }
            .addOnFailureListener {
                listener.onFail(it)
            }
    }

    fun downloadMarkets(listener: OnCollectionDownloadCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_MARKETS_COLLECTION)
            .get()
            .addOnSuccessListener {
                listener.onSuccess(it)
            }
            .addOnFailureListener {
                listener.onFail(it)
            }
    }


    fun initPhoneAuth(
        phone: String,
        activity: FragmentActivity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

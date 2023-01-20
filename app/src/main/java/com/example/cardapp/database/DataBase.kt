package com.example.cardapp.database

import androidx.fragment.app.FragmentActivity
import com.example.cardapp.interfaces.OnCollectionDownloadCompleteListener
import com.example.cardapp.interfaces.OnCompleteListener

import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.example.cardapp.models.Card

import com.example.cardapp.utils.ApiUtils
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object DataBase {
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
}

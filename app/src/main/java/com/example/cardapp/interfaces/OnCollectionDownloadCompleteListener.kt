package com.example.cardapp.interfaces

import com.google.firebase.firestore.QuerySnapshot


interface OnCollectionDownloadCompleteListener {
    fun onSuccess(documents: QuerySnapshot)
    fun onFail(e: Exception)
}

package com.example.cardapp.interfaces

import com.google.firebase.firestore.DocumentSnapshot


interface OnDocumentDownloadCompleteListener {
    fun onSuccess(document: DocumentSnapshot)
    fun onFail(e: java.lang.Exception)
}
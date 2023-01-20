package com.example.cardapp.domain.repository

import com.example.cardapp.domain.model.SmsCredential
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener

interface IAuthRepository {
    suspend fun loginUserWithName()
    suspend fun loginUserWithPhone(credential: SmsCredential): Boolean
    suspend fun createUser(
        uid: String,
        name: String,
        phone: String? = null
    )

    suspend fun getUser(uid: String, listener: OnDocumentDownloadCompleteListener)
}


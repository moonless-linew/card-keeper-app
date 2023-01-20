package com.example.cardapp.domain.repository

import com.example.cardapp.domain.model.SmsCredential

interface IAuthRepository {
    suspend fun loginUserWithName()
    suspend fun loginUserWithPhone(credential: SmsCredential): Boolean
    suspend fun createUser(
        uid: String,
        name: String,
        phone: String? = null
    )

    suspend fun getUser(uid: String): String?
}


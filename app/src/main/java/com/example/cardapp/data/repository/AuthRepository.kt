package com.example.cardapp.data.repository

import com.example.cardapp.domain.model.SmsCredential
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.example.cardapp.utils.ApiUtils
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor(
    @Named("users")
    private val usersRef: CollectionReference,
) : IAuthRepository {
    override suspend fun loginUserWithName() = suspendCoroutine { continuation ->
        Firebase.auth.signInAnonymously().addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }

    override suspend fun loginUserWithPhone(
        credential: SmsCredential,
    ): Boolean = suspendCoroutine { continuation ->
        val authCredential = PhoneAuthProvider.getCredential(credential.id, credential.code)
        Firebase.auth.signInWithCredential(authCredential).addOnSuccessListener {
            continuation.resume(true)
        }.addOnFailureListener {
            continuation.resume(false)
        }
    }

    //  почему-то коллбэк не отрабатывается
    override suspend fun createUser(
        uid: String,
        name: String,
        phone: String?,
    ) = suspendCoroutine { continuation ->
        usersRef
            .document(uid)
            .set(hashMapOf(ApiUtils.API_NAME_FIELD to name, ApiUtils.API_PHONE_FIELD to phone))
            .addOnSuccessListener {
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                continuation.resumeWithException(it)
            }
    }

    override suspend fun getUser(uid: String, listener: OnDocumentDownloadCompleteListener) {
        usersRef
            .document(uid)
            .get()
            .addOnSuccessListener {
                listener.onSuccess(it)
            }
            .addOnFailureListener {
                listener.onFail(it)
            }
    }
}

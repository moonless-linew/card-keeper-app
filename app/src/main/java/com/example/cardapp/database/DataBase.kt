package com.example.cardapp.database

import androidx.fragment.app.FragmentActivity
import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.example.cardapp.models.User
import com.example.cardapp.utils.ApiUtils
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object DataBase {

    fun loginUserWithName(listener: com.example.cardapp.interfaces.OnCompleteListener){
        Firebase.auth.signInAnonymously().addOnSuccessListener{
            listener.onSuccess()
        }.addOnFailureListener{
            listener.onFail()
        }
    }

    fun loginUserWithPhone(credential: PhoneAuthCredential, listener: com.example.cardapp.interfaces.OnCompleteListener) {
        Firebase.auth.signInWithCredential(credential).addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener{
            listener.onFail()
        }
    }

    fun createUser(uid: String, name: String?, phone: String?, listener: com.example.cardapp.interfaces.OnCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(uid)
            .set(User(name, phone))
            .addOnSuccessListener {
                listener.onSuccess()
            }
            .addOnFailureListener{
                listener.onFail()
            }
    }

    fun downloadUser(uid: String, listener: OnDownloadCompleteListener) {
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


    fun initPhoneAuth(
        phone: String,
        activity: FragmentActivity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(30, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

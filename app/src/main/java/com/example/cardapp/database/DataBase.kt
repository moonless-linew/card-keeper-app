package com.example.cardapp.database

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.example.cardapp.interfaces.OnLoginCompleteListener
import com.example.cardapp.models.User
import com.example.cardapp.utils.ApiUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Api
import java.util.concurrent.TimeUnit

object DataBase {

    fun loginUserWithName(name: String, listener: OnLoginCompleteListener){
        Firebase.auth.signInAnonymously().addOnSuccessListener{
            listener.onSuccess()
        }.addOnFailureListener{
            listener.onFail()
        }
    }

//    fun loginUserWithPhone(credential: PhoneAuthCredential) {
//        Firebase.auth.signInWithCredential(credential).
//    }
//
//    fun registerUserWithPhone() {
//
//    }

    fun createUser(name: String?, phone: String?, listener: OnLoginCompleteListener) {
        FirebaseFirestore.getInstance()
            .collection(ApiUtils.API_USERS_COLLECTION)
            .document(Firebase.auth.uid!!)
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
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

package com.example.cardapp.database

import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.example.cardapp.models.User
import com.example.cardapp.utils.ApiUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DataBase {

    companion object {
        fun registerUserWithName(
            name: String,
            listener: com.example.cardapp.interfaces.OnAuthCompleteListener
        ) {
            Firebase.auth.signInAnonymously().addOnSuccessListener {
                FirebaseFirestore.getInstance()
                    .collection(ApiUtils.API_USERS_COLLECTION)
                    .document(Firebase.auth.uid!!)
                    .set(User(name, null))
                    .addOnFailureListener {
                        listener.onFail()
                    }
                    .addOnSuccessListener {
                        listener.onSuccess()
                    }
            }.addOnFailureListener {
                listener.onAuthFail()
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
    }
}
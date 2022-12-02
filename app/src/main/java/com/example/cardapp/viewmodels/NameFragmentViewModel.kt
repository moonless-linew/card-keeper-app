package com.example.cardapp.viewmodels


import androidx.lifecycle.ViewModel

import com.example.cardapp.models.User
import com.example.cardapp.utils.ApiUtils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NameFragmentViewModel: ViewModel() {
     fun registerUserWithName(name: String, listener: com.example.cardapp.interfaces.OnCompleteListener){
         Firebase.auth.signInAnonymously().addOnSuccessListener {
             FirebaseFirestore.getInstance()
                 .collection(ApiUtils.API_USERS_COLLECTION)
                 .document(Firebase.auth.uid!!)
                 .set(User(name, ""))
                 .addOnFailureListener {
                     listener.onFail()
                 }
                 .addOnSuccessListener {
                     listener.onComplete()
                 }
         }.addOnFailureListener {
            listener.onAuthFail()
         }

    }
}
package com.example.cardapp.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.example.cardapp.models.User
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception


class MainFlowFragmentViewModel: ViewModel() {
    var user: MutableLiveData<User> = MutableLiveData<User>(User(null, null))

    fun downloadUser(uid: String){
        if(user.value?.name == null){
            DataBase.downloadUser(uid, object: OnDownloadCompleteListener{
                override fun onSuccess(document: DocumentSnapshot) {
                    user.postValue(User(document.get("name").toString(),""))

                }

                override fun onFail(e: Exception) {

                }

            })
        }
    }
}
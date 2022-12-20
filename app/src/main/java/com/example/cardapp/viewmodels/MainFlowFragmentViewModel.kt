package com.example.cardapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.example.cardapp.models.User
import com.google.firebase.firestore.DocumentSnapshot


class MainFlowFragmentViewModel: ViewModel() {
    private var _user: MutableLiveData<User?> = MutableLiveData<User?>(null)
    val user: LiveData<User?>  = _user

    fun downloadUser(uid: String){
        if(_user.value?.name == null){
            DataBase.downloadUser(uid, object: OnDocumentDownloadCompleteListener{
                override fun onSuccess(document: DocumentSnapshot) {
                    _user.postValue(User(document.get("name").toString(),"", null))
                }
                override fun onFail(e: Exception) {

                }

            })
        }
    }
}
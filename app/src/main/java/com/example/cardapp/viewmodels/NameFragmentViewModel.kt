package com.example.cardapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.viewmodels.status.NameAuthStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class NameFragmentViewModel : ViewModel() {
    private var _authStatus: MutableLiveData<NameAuthStatus> = MutableLiveData<NameAuthStatus>()
    val authStatus: LiveData<NameAuthStatus> = _authStatus

    fun registerUserWithName(
        name: String,
    ) = DataBase.loginUserWithName(object: OnCompleteListener{
        override fun onSuccess() {
            _authStatus.postValue(NameAuthStatus.Success)
            DataBase.createUser(Firebase.auth.uid!!, name, null, object: OnCompleteListener{
                override fun onSuccess() {
                    _authStatus.postValue(NameAuthStatus.Success)
                }
                override fun onFail() {
                    _authStatus.postValue(NameAuthStatus.InternetError)
                }

            })
        }
        override fun onFail() {
            _authStatus.postValue(NameAuthStatus.InternetError)
        }

    })


    fun registerUserWithPhone(name: String){
        DataBase.createUser(Firebase.auth.uid!!, name, Firebase.auth.currentUser?.phoneNumber!!, object: OnCompleteListener{
            override fun onSuccess() {
                _authStatus.postValue(NameAuthStatus.Success)
            }

            override fun onFail() {
                _authStatus.postValue(NameAuthStatus.InternetError)
            }
        })
    }
}
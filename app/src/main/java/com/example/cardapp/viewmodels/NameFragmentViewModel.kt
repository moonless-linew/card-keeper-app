package com.example.cardapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.fragments.auth.status.NameAuthStatus
import com.example.cardapp.database.DataBase
import com.example.cardapp.interfaces.OnAuthCompleteListener
import com.example.cardapp.interfaces.OnLoginCompleteListener


class NameFragmentViewModel : ViewModel() {
    private var _authStatus: MutableLiveData<NameAuthStatus> = MutableLiveData<NameAuthStatus>()
    val authStatus: LiveData<NameAuthStatus> = _authStatus

    fun registerUserWithName(
        name: String,
    ) = DataBase.loginUserWithName(name, object: OnLoginCompleteListener{
        override fun onSuccess() {
            _authStatus.postValue(NameAuthStatus.Success)
            DataBase.createUser(name, null, object: OnLoginCompleteListener{
                override fun onSuccess() {
                    _authStatus.postValue(NameAuthStatus.Success)
                }

                override fun onFail() {
                    _authStatus.postValue(NameAuthStatus.UnknownError)
                }

            })
        }
        override fun onFail() {
            _authStatus.postValue(NameAuthStatus.InternetError)
        }

    })

}
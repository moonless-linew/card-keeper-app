package com.example.cardapp.presentation.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.presentation.model.status.NameAuthStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameFragmentViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {
    private var _authStatus: MutableLiveData<NameAuthStatus> = MutableLiveData<NameAuthStatus>()
    val authStatus: LiveData<NameAuthStatus> = _authStatus

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _authStatus.postValue(NameAuthStatus.InternetError)
    }

    fun registerUserWithName(name: String) {
        viewModelScope.launch(exceptionHandler) {
            authRepository.loginUserWithName()
            val uid = Firebase.auth.uid ?: return@launch
            authRepository.createUser(uid, name, null)
            _authStatus.postValue(NameAuthStatus.Success)
        }
    }


    fun registerUserWithPhone(name: String) {
        viewModelScope.launch(exceptionHandler) {
            val uid = Firebase.auth.uid ?: return@launch
            val phoneNumber = Firebase.auth.currentUser?.phoneNumber ?: return@launch
            authRepository.createUser(uid, name, phoneNumber)
            _authStatus.postValue(NameAuthStatus.Success)
        }
    }
}

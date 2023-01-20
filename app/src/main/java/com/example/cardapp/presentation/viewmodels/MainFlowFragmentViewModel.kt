package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.example.cardapp.models.User
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFlowFragmentViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {
    private var _user: MutableLiveData<User?> = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    fun downloadUser(uid: String) {
        viewModelScope.launch {
            if (_user.value?.name == null) {
                authRepository.getUser(uid, object : OnDocumentDownloadCompleteListener {
                    override fun onSuccess(document: DocumentSnapshot) {
                        _user.postValue(User(document.get("name").toString(), "", null))
                    }

                    override fun onFail(e: Exception) {}
                })
            }
        }
    }
}

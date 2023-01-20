package com.example.cardapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.domain.model.User
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
            _user.value?.name ?: return@launch
            try {
                val name = authRepository.getUser(uid)
                _user.postValue(User(name ?: "Undefined", "", null))
            } catch (e: Exception) {

            }
        }
    }
}

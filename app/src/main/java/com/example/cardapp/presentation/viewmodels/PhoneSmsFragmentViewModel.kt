package com.example.cardapp.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardapp.domain.model.SmsCredential
import com.example.cardapp.domain.repository.IAuthRepository
import com.example.cardapp.presentation.model.status.PhoneAuthStatus
import com.example.cardapp.presentation.model.status.SmsStatus
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhoneSmsFragmentViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {
    private var _code: MutableLiveData<String> = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private var _smsStatus: MutableLiveData<SmsStatus> = MutableLiveData<SmsStatus>()
    val smsStatus: LiveData<SmsStatus> = _smsStatus

    private var _verificationId: String = String()

    private var _authStatus: MutableLiveData<PhoneAuthStatus> = MutableLiveData<PhoneAuthStatus>()
    val authStatus: LiveData<PhoneAuthStatus> = _authStatus

    fun initPhoneAuth(phone: String, activity: Activity) {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _smsStatus.postValue(SmsStatus.VerificationCompleted)
                _code.postValue(credential.smsCode)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                if (exception is FirebaseTooManyRequestsException) {
                    _smsStatus.postValue(SmsStatus.TooManyRequests)
                } else _smsStatus.postValue(SmsStatus.VerificationFailed)
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                _verificationId = id
                _smsStatus.postValue(SmsStatus.CodeSent)

            }
        }
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun loginWithPhone(code: String) {
        val credential = SmsCredential(_verificationId, code)
        viewModelScope.launch {
            val isSuccess = authRepository.loginUserWithPhone(credential)
            val status = if (isSuccess)
                PhoneAuthStatus.SuccessLogin
            else
                PhoneAuthStatus.CredentialsError
            _authStatus.postValue(status)
        }
    }

    fun checkRegistration() {
        val uid = Firebase.auth.uid ?: return
        viewModelScope.launch {
            try {
                val name = authRepository.getUser(uid)
                val phoneStatus = if (name != null) PhoneAuthStatus.Registered else PhoneAuthStatus.NotRegistered
                _authStatus.postValue(phoneStatus)

            } catch (e: Exception) {
                _authStatus.postValue(PhoneAuthStatus.InternetError)
            }
        }
    }
}

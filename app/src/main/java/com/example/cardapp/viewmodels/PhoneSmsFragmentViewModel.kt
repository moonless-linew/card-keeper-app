package com.example.cardapp.viewmodels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase
import com.example.cardapp.fragments.auth.status.PhoneAuthStatus
import com.example.cardapp.fragments.auth.status.SmsStatus
import com.example.cardapp.interfaces.OnCompleteListener
import com.example.cardapp.interfaces.OnDocumentDownloadCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class PhoneSmsFragmentViewModel : ViewModel() {
    private var _code: MutableLiveData<String> = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private var _smsStatus: MutableLiveData<SmsStatus> = MutableLiveData<SmsStatus>()
    val smsStatus: LiveData<SmsStatus> = _smsStatus

    private var _verificationId: String = String()

    private var _authStatus: MutableLiveData<PhoneAuthStatus> = MutableLiveData<PhoneAuthStatus>()
    val authStatus: LiveData<PhoneAuthStatus> = _authStatus

    fun initPhoneAuth(
        phone: String,
        activity: FragmentActivity,

        ) = DataBase.initPhoneAuth(
        phone,
        activity,
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                _smsStatus.postValue(SmsStatus.VerificationCompleted)
                _code.postValue(p0.smsCode)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                if (p0 is FirebaseTooManyRequestsException) {
                    _smsStatus.postValue(SmsStatus.TooManyRequests)
                } else _smsStatus.postValue(SmsStatus.VerificationFailed)
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                _verificationId = p0
                _smsStatus.postValue(SmsStatus.CodeSent)

            }

        })

    fun loginWithPhone(code: String) {
        val credential =
            PhoneAuthProvider.getCredential(
                _verificationId,
                code
            )
        DataBase.loginUserWithPhone(credential, object : OnCompleteListener {
            override fun onSuccess() {
                _authStatus.postValue(PhoneAuthStatus.SuccessLogin)
                checkRegistration()
            }

            override fun onFail() {
                _authStatus.postValue(PhoneAuthStatus.CredentialsError)
            }

        })
    }

    fun checkRegistration() {
        DataBase.downloadUser(Firebase.auth.uid!!, object : OnDocumentDownloadCompleteListener {
            override fun onSuccess(document: DocumentSnapshot) {
                if (document.getField<String>("name") != null) {
                    _authStatus.postValue(PhoneAuthStatus.Registered)
                } else {
                    _authStatus.postValue(PhoneAuthStatus.NotRegistered)
                }
            }

            override fun onFail(e: Exception) {
                _authStatus.postValue(PhoneAuthStatus.InternetError)
            }

        })
    }
}

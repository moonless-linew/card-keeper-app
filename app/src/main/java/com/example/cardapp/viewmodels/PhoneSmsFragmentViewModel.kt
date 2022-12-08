package com.example.cardapp.viewmodels

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.cardapp.R
import com.example.cardapp.fragments.auth.status.SmsStatus
import com.example.cardapp.database.DataBase
import com.example.cardapp.fragments.auth.activityNavController
import com.example.cardapp.fragments.auth.navigateSafely
import com.example.cardapp.interfaces.OnDownloadCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class PhoneSmsFragmentViewModel : ViewModel() {
    private var _code: MutableLiveData<String> = MutableLiveData<String>("")
    val code: LiveData<String> = _code

    private var _smsStatus: MutableLiveData<SmsStatus> = MutableLiveData<SmsStatus>()
    val smsStatus: LiveData<SmsStatus> = _smsStatus

    private var _verificationId: MutableLiveData<String> = MutableLiveData<String>()
    val verificationId: LiveData<String> = _verificationId

    private var _authStatus: MutableLiveData<SmsStatus> = MutableLiveData<SmsStatus>()
    val authStatus: LiveData<SmsStatus> = _authStatus

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
                _verificationId.postValue(p0)
                _smsStatus.postValue(SmsStatus.CodeSent)

            }

        })

    fun loginWithPhone() {
        val credential =
            PhoneAuthProvider.getCredential(
                _verificationId.value ?: "0",
                _code.value ?: "0")

    }



//    private fun signInWithCredential(credential: PhoneAuthCredential) {
//        Firebase.auth.signInWithCredential(credential).addOnSuccessListener {
//            DataBase.downloadUser(it.user?.uid!!, object : OnDownloadCompleteListener {
//                override fun onSuccess(document: DocumentSnapshot) {
//                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
//                }
//
//                override fun onFail(e: Exception) {
//                    findNavController().navigateSafely(R.id.action_smsFragment_to_nameFragment)
//                }
//
//            })
//        }.addOnFailureListener {
//            Toast.makeText(requireActivity(), getString(R.string.wrong_code), Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
}

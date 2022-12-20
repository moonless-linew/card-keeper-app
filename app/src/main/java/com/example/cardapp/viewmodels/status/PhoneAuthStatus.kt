package com.example.cardapp.viewmodels.status

sealed class PhoneAuthStatus{
    object SuccessLogin: PhoneAuthStatus()
    object NotRegistered: PhoneAuthStatus()
    object Registered: PhoneAuthStatus()
    object CredentialsError: PhoneAuthStatus()
    object InternetError: PhoneAuthStatus()
    object UnknownError: PhoneAuthStatus()
}

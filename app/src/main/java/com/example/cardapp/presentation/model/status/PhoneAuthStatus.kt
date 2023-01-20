package com.example.cardapp.presentation.model.status

import androidx.annotation.StringRes
import com.example.cardapp.R

sealed interface PhoneAuthStatus{
    object SuccessLogin: PhoneAuthStatus
    object NotRegistered: PhoneAuthStatus
    object Registered: PhoneAuthStatus

    abstract class Error(@StringRes val stringId: Int): PhoneAuthStatus
    object CredentialsError: Error(R.string.wrong_code)
    object InternetError: Error(R.string.internet_error)
    object UnknownError: Error(R.string.error)
}

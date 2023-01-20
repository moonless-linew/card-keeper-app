package com.example.cardapp.presentation.model.status

import androidx.annotation.StringRes
import com.example.cardapp.R

sealed interface SmsStatus{
    object CodeSent : SmsStatus
    object VerificationCompleted : SmsStatus
    abstract class Error(@StringRes val stringId: Int): SmsStatus
    object TooManyRequests: Error(R.string.too_much_requests)
    object VerificationFailed: Error(R.string.error)
}

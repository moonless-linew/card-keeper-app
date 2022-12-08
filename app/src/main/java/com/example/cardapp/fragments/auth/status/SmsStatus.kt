package com.example.cardapp.fragments.auth.status

sealed class SmsStatus{
    object CodeSent : SmsStatus()
    object VerificationCompleted : SmsStatus()
    object TooManyRequests : SmsStatus()
    object VerificationFailed: SmsStatus()
}

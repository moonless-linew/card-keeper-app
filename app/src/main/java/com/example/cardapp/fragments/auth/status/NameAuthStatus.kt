package com.example.cardapp.fragments.auth.status

sealed class NameAuthStatus {
    object InternetError : NameAuthStatus()
    object UnknownError : NameAuthStatus()
    object Success : NameAuthStatus()
}
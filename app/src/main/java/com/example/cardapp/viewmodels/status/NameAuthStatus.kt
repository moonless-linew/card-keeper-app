package com.example.cardapp.viewmodels.status

sealed class NameAuthStatus {
    object InternetError : NameAuthStatus()
    object UnknownError : NameAuthStatus()
    object Success : NameAuthStatus()
}
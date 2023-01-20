package com.example.cardapp.presentation.model.status

sealed class NameAuthStatus {
    object InternetError : NameAuthStatus()
    object Success : NameAuthStatus()
}

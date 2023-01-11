package com.example.cardapp.viewmodels.status

sealed class CardUploadStatus{
    object Success: CardUploadStatus()
    object Fail: CardUploadStatus()
}

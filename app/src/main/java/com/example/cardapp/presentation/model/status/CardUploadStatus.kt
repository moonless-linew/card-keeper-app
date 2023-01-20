package com.example.cardapp.presentation.model.status

sealed class CardUploadStatus{
    object Success: CardUploadStatus()
    object Fail: CardUploadStatus()
    object Null: CardUploadStatus()
}

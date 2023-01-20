package com.example.cardapp.presentation.model.status

sealed class CompletableTaskStatus{
    object Success: CompletableTaskStatus()
    object Fail: CompletableTaskStatus()
}

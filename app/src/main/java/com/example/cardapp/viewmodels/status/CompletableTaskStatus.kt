package com.example.cardapp.viewmodels.status

sealed class CompletableTaskStatus{
    object Success: CompletableTaskStatus()
    object Fail: CompletableTaskStatus()
}
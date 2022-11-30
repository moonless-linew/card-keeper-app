package com.example.cardapp.applications

import android.app.Application
import com.google.android.material.color.DynamicColors

class CardApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
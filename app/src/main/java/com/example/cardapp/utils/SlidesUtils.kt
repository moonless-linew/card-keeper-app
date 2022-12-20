package com.example.cardapp.utils

import com.example.cardapp.R
import com.example.cardapp.models.Slide

object SlidesUtils {
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val IMAGE = "image"
    val SLIDES = listOf(
        Slide(
            "Functional",
            "Do whatever you want",
            R.drawable.ic_baseline_credit_card_24
        ),
        Slide(
            "Easy to use",
            "Show this app your grandmother",
            R.drawable.ic_baseline_mood_24
        ),
        Slide(
            "Smart",
            "Can choose card, using GPS",
            R.drawable.ic_baseline_location_on_24
        ),
        Slide("Fast",
        "Made with love",
        R.drawable.ic_baseline_flash_on_24),



    )
}
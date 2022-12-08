package com.example.cardapp.utils

import com.example.cardapp.R
import com.example.cardapp.models.Slide

object SlidesUtils {
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val IMAGE = "image"
    val SLIDES = listOf(
        Slide(
            "Light",
            "Your Nokia3310 can run this app",
            R.drawable.ic_baseline_help_24
        ),
        Slide(
            "Easy",
            "It can use even your grandmother",
            R.drawable.ic_baseline_credit_card_24
        ),
        Slide(
            "Smart",
            "Can choose card, using GPS \n (smarter than your ex)",
            R.drawable.ic_baseline_location_on_24
        )


    )
}
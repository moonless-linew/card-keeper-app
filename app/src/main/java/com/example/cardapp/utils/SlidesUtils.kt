package com.example.cardapp.utils

import android.content.Context
import com.example.cardapp.R
import com.example.cardapp.models.Slide

object SlidesUtils {
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val IMAGE = "image"
    fun getSlides(context: Context): List<Slide>{
        return listOf(
            Slide(
                context.getString(R.string.functional),
                context.getString(R.string.do_whatever),
                R.drawable.ic_baseline_credit_card_24
            ),
            Slide(
                context.getString(R.string.easy_to_use),
                context.getString(R.string.show_this_app_your_grandmother),
                R.drawable.ic_baseline_mood_24
            ),
            Slide(
                context.getString(R.string.smart),
                context.getString(R.string.gps_feature),
                R.drawable.ic_baseline_location_on_24
            ),
            Slide(context.getString(R.string.fast),
                context.getString(R.string.made_with_love),
                R.drawable.ic_baseline_flash_on_24),

            )
    }
}
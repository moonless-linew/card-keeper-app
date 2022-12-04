package com.example.cardapp.models

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Slide(
    val title: String?,
    val description: String?,
    @DrawableRes val image: Int
): Parcelable


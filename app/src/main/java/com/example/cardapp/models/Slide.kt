package com.example.cardapp.models

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize
import java.text.FieldPosition

@Parcelize
data class Slide(
    val title: String?,
    val description: String?,
    val position: Int,
    @DrawableRes val image: Int
): Parcelable


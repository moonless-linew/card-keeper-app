package com.example.cardapp.models

import androidx.annotation.DrawableRes

data class Card(val market: String?, val description: String?, @DrawableRes val image: Int)

package com.example.cardapp.domain.model

data class Card(
    val id: String? = null,
    val marketID: String? = null,
    var marketNetwork: MarketNetwork? = null,
    val codeType: String? = null,
)

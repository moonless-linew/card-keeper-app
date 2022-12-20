package com.example.cardapp.models

data class Card(val id: String?, val marketID: String?){
    constructor() : this(null, null)
}

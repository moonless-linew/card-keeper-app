package com.example.cardapp.models


class Card(){
    constructor(id: String?, marketID: String?): this(){
        this.id = id
        this.marketID = marketID
    }
    var id: String? = null
    var marketID: String? = null


    var market: Market? = null
}

package com.example.inventarasit

data class Produs(
    val codMarfa: String,
    val nume: String,
    val locatie: String,
    val codBare: String,
    val um: String,
    var stocInit: Int,
    var stocScan: Int = 0
)

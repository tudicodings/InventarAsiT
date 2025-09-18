package com.example.inventarasit

data class Produs(
    val codMarfa: String,
    val nume: String,
    val locatie: String,
    val codBare: String,
    val um: String,
    var stocInit: Double,
    var stocScan: Double = 0.0
)

package com.aruana.currencyprice.networking

data class CurrencyModel(var base: String,
                         val rate: Double = 1.toDouble(),
                         var countryName: String = "",
                         var countryFlag: String = "",
                         var price: String = "")
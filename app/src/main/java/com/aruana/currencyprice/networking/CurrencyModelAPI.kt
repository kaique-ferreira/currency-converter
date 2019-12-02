package com.aruana.currencyprice.networking

data class CurrencyModelAPI(val base: String, val date: String, val rates: Map<String, Double>)



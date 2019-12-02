package com.aruana.currencyprice.networking

data class CountryModelAPI(val currencies: List<CountryCurrencyModelAPI>, val flag: String)

data class CountryCurrencyModelAPI(val code: String, val name: String)
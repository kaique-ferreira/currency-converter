package com.aruana.currencyprice.networking

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("latest")
    fun priceByBaseCurrency(@Query("base") currencyCode: String): Single<CurrencyModelAPI>

    companion object {
        const val API_ENDPOINT = "https://revolut.duckdns.org/"
    }
}
package com.aruana.currencyprice.networking

import io.reactivex.Single
import retrofit2.http.GET

interface CountryService {

    @GET("all")
    fun countryData(): Single<List<CountryModelAPI>>

    companion object {
        const val API_ENDPOINT = "https://restcountries.eu/rest/v2/"
    }
}
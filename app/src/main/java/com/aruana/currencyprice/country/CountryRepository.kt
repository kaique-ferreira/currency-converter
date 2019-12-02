package com.aruana.currencyprice.country

import io.reactivex.Single

interface CountryRepository {

    fun getCountryData(): Single<List<CountryModel>>
}
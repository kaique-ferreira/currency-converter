package com.aruana.currencyprice.country

import com.aruana.currencyprice.networking.CountryModelAPI
import com.aruana.currencyprice.networking.CountryService
import io.reactivex.Single
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(private val countryService: CountryService) : CountryRepository {

    override fun getCountryData(): Single<List<CountryModel>> = countryService.countryData().map { mapApiResult(it) }

    private fun mapApiResult(apiResult: List<CountryModelAPI>) =
            apiResult.map {
                CountryModel(name = it.currencies.firstOrNull()?.name ?: "",
                        flagUrl = it.flag,
                        currencyCode = it.currencies.firstOrNull()?.code ?: "")
            }
}
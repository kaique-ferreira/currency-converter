package com.aruana.currencyprice.currency

import com.aruana.currencyprice.country.CountryModel
import com.aruana.currencyprice.country.CountryRepository
import com.aruana.currencyprice.networking.CurrencyModel
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class FindCurrencyRatesImpl @Inject constructor(private val currencyRepository: CurrencyRepository,
                                                private val countryRepository: CountryRepository) : FindCurrencyRates() {

    private var cachedCountriesInfo: List<CountryModel> = emptyList()

    override fun createSingleUseCase(params: Params): Single<List<CurrencyModel>> {
        return if (cachedCountriesInfo.isEmpty()) {
            Single.zip(countryRepository.getCountryData(), currencyRepository.getCurrenciesRatesForBaseCurrency(params.code),
                    BiFunction<List<CountryModel>, List<CurrencyModel>, List<CurrencyModel>> { countriesInfo, currencies ->
                        cachedCountriesInfo = countriesInfo
                        associateCountryWithCurrency(currencies, params.code, params.basePrice)
                    })
        } else {
            currencyRepository.getCurrenciesRatesForBaseCurrency(params.code)
                    .map { associateCountryWithCurrency(it, params.code, params.basePrice) }
        }
    }

    private fun associateCountryWithCurrency(currencies: List<CurrencyModel>,
                                             baseCode: String,
                                             basePrice: Double): List<CurrencyModel> {
        currencies.forEach { currency ->
            cachedCountriesInfo.find { it.currencyCode == currency.base }?.let { country ->
                currency.countryName = country.name
                currency.countryFlag = country.flagUrl
            }

            currency.price = (basePrice * currency.rate).formatAsCurrency()
        }

        val currenciesWithBase = mutableListOf<CurrencyModel>()
        val currentBaseCurrency = buildCurrentBaseCurrency(baseCode, basePrice)

        currenciesWithBase.add(currentBaseCurrency)
        currenciesWithBase.addAll(currencies)
        return currenciesWithBase
    }

    private fun buildCurrentBaseCurrency(code: String, basePrice: Double): CurrencyModel {
        //Just a little workaround because I used a third party api to load the country images, but this api does not understand a region like european
        // union, I only handled it  when the app starts. Same problem with US dollars. I did not dive too deep into this because in a real app this should
        // be solved int the api
        val country = if (code == DEFAULT_CURRENCY) {
            CountryModel(name = "Euro", flagUrl = "https://upload.wikimedia.org/wikipedia/commons/b/b7/Flag_of_Europe.svg", currencyCode = "EUR")
        } else {
            cachedCountriesInfo.find { it.currencyCode == code }
        }

        return CurrencyModel(base = code,
                rate = 1.toDouble(),
                price = basePrice.formatAsCurrency(),
                countryName = country?.name ?: "",
                countryFlag = country?.flagUrl ?: "")
    }
}

fun Double.formatAsCurrency() = "%.2f".format(this)
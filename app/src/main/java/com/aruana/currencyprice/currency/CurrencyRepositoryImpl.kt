package com.aruana.currencyprice.currency

import com.aruana.currencyprice.networking.CurrencyModel
import com.aruana.currencyprice.networking.CurrencyService
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val currencyService: CurrencyService) : CurrencyRepository {

    override fun getCurrenciesRatesForBaseCurrency(baseCurrency: String) = currencyService.priceByBaseCurrency(baseCurrency).map {
        it.rates.map { pairCodeRating ->
            CurrencyModel(base = pairCodeRating.key, rate = pairCodeRating.value)
        }
    }
}
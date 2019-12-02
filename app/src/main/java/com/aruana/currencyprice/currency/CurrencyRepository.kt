package com.aruana.currencyprice.currency

import com.aruana.currencyprice.networking.CurrencyModel
import io.reactivex.Single

interface CurrencyRepository {

    fun getCurrenciesRatesForBaseCurrency(baseCurrency: String): Single<List<CurrencyModel>>
}
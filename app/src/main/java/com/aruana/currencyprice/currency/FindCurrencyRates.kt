package com.aruana.currencyprice.currency

import com.aruana.currencyprice.domain.SingleUseCase
import com.aruana.currencyprice.networking.CurrencyModel
import io.reactivex.disposables.Disposable

abstract class FindCurrencyRates : SingleUseCase<List<CurrencyModel>, FindCurrencyRates.Params> {

    override var disposable: Disposable? = null

    data class Params(val code: String, val basePrice: Double)
}
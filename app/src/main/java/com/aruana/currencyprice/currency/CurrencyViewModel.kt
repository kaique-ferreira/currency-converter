package com.aruana.currencyprice.currency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aruana.currencyprice.networking.CurrencyModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val DEFAULT_CURRENCY = "EUR"
const val DEFAULT_BASE_AMMOUNT = 100.toDouble()

class CurrencyViewModel @Inject constructor(private val findCurrencyRates: FindCurrencyRates,
                                            val adapter: CurrenciesListAdapter) : ViewModel() {

    private var repeatingCurrencyRateCheck: Disposable? = null

    private val _currencies = MutableLiveData<List<CurrencyModel>>()

    val state = MutableLiveData<State>()

    override fun onCleared() {
        repeatingCurrencyRateCheck?.dispose()
        findCurrencyRates.dispose()
    }

    fun updateCurrencyRateList(baseItem: CurrencyModel? = null) {
        state.value = State.LOADING

        repeatingCurrencyRateCheck?.dispose()
        repeatingCurrencyRateCheck = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe {
                    findCurrencyRates.invoke(
                            onSuccess = {
                                _currencies.value = it
                                adapter.submitList(it)
                            },
                            onError = {
                                //TODO add error state to handle in the view
                                if (state.value == State.LOADING) {
                                    state.value = State.FINISHED_LOADING
                                }
                            },
                            onFinished = {
                                if (state.value == State.LOADING) {
                                    state.value = State.FINISHED_LOADING
                                }
                            },
                            params = FindCurrencyRates.Params(baseItem?.base ?: DEFAULT_CURRENCY,
                                    baseItem?.price?.toDoubleOrNull() ?: DEFAULT_BASE_AMMOUNT))
                }
    }

    fun changeBaseCurrency(index: Int) {
        val item = _currencies.value?.get(index)
        updateCurrencyRateList(item)
    }

    fun calculateNewPrices(index: Int, priceString: String) {
        val item = _currencies.value?.get(index)!!

        val newBasePrice = (priceString.toDoubleOrNull() ?: DEFAULT_BASE_AMMOUNT).div(item.rate).toString()

        val firstItem = _currencies.value?.get(0)!!
        val newBaseItem = CurrencyModel(base = firstItem.base, price = newBasePrice)

        updateCurrencyRateList(newBaseItem)
    }

    fun stopRequestingNewCurrencyData() {
        repeatingCurrencyRateCheck?.dispose()
    }

    enum class State {
        LOADING,
        FINISHED_LOADING
    }
}
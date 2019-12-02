package com.aruana.currencyprice.di

import com.aruana.currencyprice.country.CountryRepository
import com.aruana.currencyprice.country.CountryRepositoryImpl
import com.aruana.currencyprice.currency.CurrencyRepository
import com.aruana.currencyprice.currency.CurrencyRepositoryImpl
import com.aruana.currencyprice.currency.FindCurrencyRates
import com.aruana.currencyprice.currency.FindCurrencyRatesImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CurrencyModule {

    @Binds
    abstract fun provideFindCurrencyRatesUseCase(useCase: FindCurrencyRatesImpl): FindCurrencyRates

    @Binds
    abstract fun provideCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    abstract fun provideCountryRepository(repository: CountryRepositoryImpl): CountryRepository
}
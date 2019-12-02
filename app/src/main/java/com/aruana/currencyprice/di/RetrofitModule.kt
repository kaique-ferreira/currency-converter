package com.aruana.currencyprice.di

import com.aruana.currencyprice.networking.CountryService
import com.aruana.currencyprice.networking.CurrencyService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideCurrencyService(): CurrencyService = Retrofit.Builder()
            .baseUrl(CurrencyService.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(CurrencyService::class.java)

    @Provides
    @Singleton
    fun provideCountryService(): CountryService = Retrofit.Builder()
            .baseUrl(CountryService.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(CountryService::class.java)
}
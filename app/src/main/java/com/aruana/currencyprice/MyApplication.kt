package com.aruana.currencyprice

import android.app.Application
import com.aruana.currencyprice.di.AppComponent
import com.aruana.currencyprice.di.DaggerAppComponent

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
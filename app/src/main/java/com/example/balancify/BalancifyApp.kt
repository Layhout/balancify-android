package com.example.balancify

import android.app.Application
import com.example.balancify.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BalancifyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BalancifyApp)
            modules(appModule)
        }
    }
}
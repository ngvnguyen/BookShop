package com.ptit.bookshop

import android.app.Application
import com.ptit.bookshop.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookApplication)
            modules(module)
        }
    }
}
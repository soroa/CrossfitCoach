package com.example.mac.crossfitcoach

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber


class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
    }
}
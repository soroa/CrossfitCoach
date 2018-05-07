package com.example.mac.crossfitcoach

import android.app.Application
import com.example.mac.crossfitcoach.communication.ble.BleClient
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.facebook.stetho.Stetho
import timber.log.Timber


class MyApplication: Application(){

    val bleClient by lazy {
        BleClient(applicationContext)
    }

    val bleServer by lazy {
        BleServer(applicationContext)
    }


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
    }



}
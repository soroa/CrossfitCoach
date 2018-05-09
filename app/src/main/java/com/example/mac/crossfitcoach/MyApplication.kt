package com.example.mac.crossfitcoach

import android.app.Application
import android.util.Log
import com.example.mac.crossfitcoach.communication.ble.BleClient
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.facebook.stetho.Stetho
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MyApplication : Application() {

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
        TrueTimeRx.build()
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .subscribe({ date -> Log.v("Andrea", "TrueTime was initialized and we have a time: ${date.time}") }) { throwable -> throwable.printStackTrace() }
    }
}
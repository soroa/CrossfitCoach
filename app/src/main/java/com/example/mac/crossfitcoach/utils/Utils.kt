package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.instacart.library.truetime.TrueTime
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun addTouchEffect(view: View) {
    view.setOnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN ->
                view.alpha = 0.5f
            MotionEvent.ACTION_UP ->
                view.alpha = 1f
            else -> view.alpha = 1f
        }
        false
    }
}

fun disableTouch(view: View) {
    view.setOnTouchListener { v, event -> true }
}

fun enableTouch(view: View) {
    view.setOnTouchListener { v, event -> false }
}

fun vibrate(context: Context, durationMs: Long) {
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}


fun checkIfClockIsSynched(context: Context) {
    if (!TrueTimeRx.isInitialized()) {
        synchClock(context)
    } else {
        Toast.makeText(context, "Clock is synched", Toast.LENGTH_LONG).show()
    }
}

fun synchClock(context: Context) {
    TrueTimeRx.build()
            .withSharedPreferences(context.applicationContext)
            .initializeRx("time.google.com")
            .subscribeOn(Schedulers.io())
            .subscribe(
                    { date ->
                        Log.v("Andrea", "TrueTime was initialized and we have a time: ${date.time}")
                        runOnMainThred { Toast.makeText(context, "Clock synched! You may disconnect from internet", Toast.LENGTH_SHORT).show() }
                    })
            { throwable ->
                runOnMainThred { Toast.makeText(context, "Clock synch failed, check your internet connection", Toast.LENGTH_LONG).show() }
            }
}

fun runOnMainThred(foo: () -> Unit) {
    Completable.fromAction(foo)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}
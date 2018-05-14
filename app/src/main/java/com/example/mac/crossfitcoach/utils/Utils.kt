package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View

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


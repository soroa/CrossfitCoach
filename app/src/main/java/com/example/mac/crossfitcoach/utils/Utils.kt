package com.example.mac.crossfitcoach.utils

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

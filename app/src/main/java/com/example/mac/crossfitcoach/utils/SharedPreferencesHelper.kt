package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.SensorReading

class SharedPreferencesHelper(val context: Context) {
    val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences("crossfit_coach_shared_preferences", Context.MODE_PRIVATE)
    }

    fun getSmartWatchPosition(): Int {
        return sharedPrefs.getInt(context.getString(R.string.smart_watch_position), SensorReading.WRIST)
    }

    fun setSmartwatchPosition(position: Int) {
        with(sharedPrefs.edit()) {
            putInt(context.getString(R.string.smart_watch_position), position)
            commit()
        }
    }

    fun isFirstTime(): Boolean {
        return sharedPrefs.getBoolean(context.getString(R.string.is_first_time), true)
    }

    fun setIsFirstTime(isFirstTime: Boolean) {
        with(sharedPrefs.edit()) {
            putBoolean(context.getString(R.string.is_first_time), isFirstTime)
            commit()
        }
    }
}
package com.example.mac.crossfitcoach.utils

import android.content.Context
import java.util.*

class SynchedCalendar {
    companion object {
        fun getInstance(context: Context): Calendar {
            val calendar = Calendar.getInstance()
            val clockDifference = SharedPreferencesHelper(context).getClientTimestampDifference()
            calendar.add(Calendar.MILLISECOND, clockDifference.toInt())
            return calendar
        }
    }
}
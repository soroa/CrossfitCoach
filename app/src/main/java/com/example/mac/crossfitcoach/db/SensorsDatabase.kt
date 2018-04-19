package com.example.mac.crossfitcoach.db

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database


@Database(entities = arrayOf(SensorReading::class), version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun sensorReadingsDao(): SensorReadingsDao
}
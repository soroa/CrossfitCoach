package com.example.mac.crossfitcoach.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query


@Dao
interface SensorReadingsDao {
    @Insert(onConflict = REPLACE)
    fun save(sensorReading: SensorReading)

    @Query("SELECT * FROM sensorreading WHERE id = :sensorReadingId")
    fun load(sensorReadingId: Int): LiveData<SensorReading>
}

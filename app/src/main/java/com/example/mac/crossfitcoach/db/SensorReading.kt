package com.example.mac.crossfitcoach.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(foreignKeys = [(ForeignKey(entity = Session::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("session_id"),
        onDelete = ForeignKey.CASCADE
))], tableName = "sensor_readings")
data class SensorReading(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        @ColumnInfo(name = "sensor_type")
        val sensorType: Int,
        @ColumnInfo(name = "values")
        val values: Array<Float>,
        @ColumnInfo(name = "time")
        val time: String,
        @ColumnInfo(name = "session_id")
        val sessionId: Int,
        @ColumnInfo(name = "sensor_position")
        val sensorPosition: Int = 0)
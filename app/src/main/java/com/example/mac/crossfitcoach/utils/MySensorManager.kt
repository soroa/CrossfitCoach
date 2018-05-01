package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class MySensorManager(context: Context, sensorCodes: Array<Int>) : SensorEventListener {

    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensors = mutableListOf<Sensor>()
    private val sensorReadingsLocal = mutableListOf<SensorReading>()

    init {
        for (sensorCode in sensorCodes) {
            val sensor = sensorManager.getDefaultSensor(sensorCode)
            if (sensor != null) {
                sensors.add(sensor)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        sensorReadingsLocal.add(SensorReading(sensorEvent!!.sensor.type
                , sensorEvent.values.copyOf()
                , 0
                , Calendar.getInstance().time, SensorReading.WRIST))
    }

    fun stopSensing() {
        sensorManager.unregisterListener(this)
    }

    fun deleteCachedRecordings() {
        sensorReadingsLocal.clear()
    }

    fun getReadings(): List<SensorReading> {
        return sensorReadingsLocal
    }

    fun startSensing() {
        sensorReadingsLocal.clear()
        for (sensor in sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }
}
package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class MySensorManager(context: Context, sensorCodes: Array<Int>, @Exercise.ExerciseCode var  exerciseCode: Int) : SensorEventListener {

    val sensorManager: SensorManager
    val sensors = mutableListOf<Sensor>()
    val sensorReadingsLocal = mutableListOf<SensorReading>()

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        for (sensorCode in sensorCodes) {

            val sensor = sensorManager.getDefaultSensor(sensorCode)
            if (sensor != null) {
                sensors.add(sensor)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorReadingsLocal.add(SensorReading(sensorEvent!!.sensor.type
                , sensorEvent.values.asList()
                , 0
                , exerciseCode
                , Calendar.getInstance().time))
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
        for (sensor in sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }
}
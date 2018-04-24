package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.mac.crossfitcoach.dbjava.SensorReading
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MySensorManager(context: Context, sensorCodes: Array<Int>) : SensorEventListener {

    val sensorManager: SensorManager
    val sensors = mutableListOf<Sensor>()
    val sensorReadingsLocal = mutableListOf<SensorReading>()

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        for (sensorCode in sensorCodes) {
            sensors.add(sensorManager.getDefaultSensor(sensorCode))
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorReadingsLocal.add(SensorReading(sensorEvent!!.sensor.type
                , sensorEvent.values
                , 0
                , 0
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
        var allSensorsCompletable: Completable? = null
        for (sensor in sensors) {
            if (allSensorsCompletable == null) {
                allSensorsCompletable = Completable.fromAction {
                    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
                }.subscribeOn(Schedulers.newThread())
            } else {
                allSensorsCompletable.mergeWith(Completable.fromAction { sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST) })
            }
            allSensorsCompletable?.subscribe()
        }
    }
}
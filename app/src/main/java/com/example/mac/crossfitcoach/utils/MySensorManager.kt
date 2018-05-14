package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.instacart.library.truetime.TrueTime
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MySensorManager(val context: Context, sensorCodes: Array<Int>) : SensorEventListener {

    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensors = mutableListOf<Sensor>()
    private val sensorReadingsLocal = mutableListOf<SensorReading>()
    private val position = SharedPreferencesHelper(context).getSmartWatchPosition()

    var rep = 0

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
                //exercise id will be set later
                , 0
                , TrueTime.now(),
                position,
                rep
        ))
    }

    fun stopSensing() {
        if (position == SensorReading.WRIST) {
            vibrator?.dispose()
        }
        sensorManager.unregisterListener(this)
    }

    fun deleteCachedRecordings() {
        sensorReadingsLocal.clear()
    }

    fun getReadings(): MutableList<SensorReading> {
        return sensorReadingsLocal
    }

    fun startSensing(exerciseCode: Int) {
        sensorReadingsLocal.clear()
        rep = 0
        startRepCounter(exerciseCode)
        for (sensor in sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    private var vibrator: Disposable? = null

    private fun startRepCounter(exerciseCode: Int) {
        val period = Exercise.codeToAverageRepTime.get(exerciseCode)
        vibrator = Observable.interval(0, period!!, TimeUnit.SECONDS)
                .subscribe {
                    rep++
                    if (position == SensorReading.WRIST) {
                        vibrate(context, 200)
                    }
                }
    }
}
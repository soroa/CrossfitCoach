package com.example.mac.crossfitcoach.utils

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.instacart.library.truetime.TrueTime
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class MySensorManager(context: Context, sensorCodes: Array<Int>) : SensorEventListener {

    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensors = mutableListOf<Sensor>()
    private val sensorReadingsLocal = mutableListOf<SensorReading>()
    private val sensorPosition: Int = SharedPreferencesHelper(context).getSmartWatchPosition()
    private var rep = 0

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
                sensorPosition,
                rep
        ))
    }

    fun stopSensing() {
        vibrator!!.dispose()
        sensorManager.unregisterListener(this)
    }

    fun deleteCachedRecordings() {
        sensorReadingsLocal.clear()
    }

    fun getReadings(): List<SensorReading> {
        return sensorReadingsLocal
    }

    fun startSensing(context: Context) {
        sensorReadingsLocal.clear()
        rep = 0
        for (sensor in sensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
        if (SharedPreferencesHelper(context).getSmartWatchPosition() == SensorReading.WRIST) {
            startVibrations(context)
        }
    }

    private var vibrator: Disposable? = null

    private fun startVibrations(context: Context) {
        vibrator = Observable.interval(0, 2, TimeUnit.SECONDS)
                .subscribe {
                    rep++
                    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                    }

                }

    }
}
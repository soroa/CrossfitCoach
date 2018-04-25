package com.example.mac.crossfitcoach.record_session

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.persistence.room.Room
import android.hardware.Sensor
import android.os.Bundle
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.utils.INT_VALUE
import com.example.mac.crossfitcoach.utils.MySensorManager
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.*

class RecordExerciseViewModel(application: Application, args: Bundle) : AndroidViewModel(application) {

    private var db: SensorDatabase = Room.databaseBuilder(getApplication(),
            SensorDatabase::class.java, "sensor_readings").build()
    private var sensorManager: MySensorManager
    private val exerciseCode: Int = args.getInt(INT_VALUE)
    private var exerciseStartTime: Date? = null
    private var exerciseEndTime: Date? = null

    init {
        sensorManager = MySensorManager(getApplication(),
                arrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_PROXIMITY),
                exerciseCode)
    }

    fun startRecording() {
        exerciseStartTime = Calendar.getInstance().time
        sensorManager.startSensing()
    }

    fun stopRecording() {
        exerciseStartTime = Calendar.getInstance().time
        sensorManager.stopSensing()
    }

    fun deleteRecording() {
        sensorManager.deleteCachedRecordings()
    }

    fun saveRecording() {
        Completable.fromAction {
            db.sensorReadingsDao().saveAll(sensorManager.getReadings())
            db.exerciseDao().save(Exercise(exerciseStartTime, exerciseEndTime, exerciseCode))
        }.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe()
    }
}
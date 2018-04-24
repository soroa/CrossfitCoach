package com.example.mac.crossfitcoach.record_session

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.persistence.room.Room
import android.hardware.Sensor
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.utils.MySensorManager
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private var db: SensorDatabase
    private var sensorManager: MySensorManager

    init {
        db = Room.databaseBuilder(getApplication(),
                SensorDatabase::class.java, "sensor_readings").build()
        sensorManager = MySensorManager(getApplication(), arrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE))
    }


    fun startRecording() {
        sensorManager.startSensing()
    }

    fun stopRecording() {
        sensorManager.stopSensing()
    }

    fun deleteRecording() {
        sensorManager.deleteCachedRecordings()
    }

    fun saveRecording() {
        Completable.fromAction {
            db.sensorReadingsDao().saveAll(sensorManager.getReadings())
        }.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe()
    }

}
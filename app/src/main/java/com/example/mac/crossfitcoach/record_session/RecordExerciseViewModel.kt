package com.example.mac.crossfitcoach.record_session

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.persistence.room.Room
import android.hardware.Sensor
import com.example.mac.crossfitcoach.db.SensorsDatabase
import com.example.mac.crossfitcoach.utils.MySensorManager

class RecordExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private var db: SensorsDatabase
    private var sensorManager: MySensorManager

    init {
        db = Room.databaseBuilder(getApplication(),
                SensorsDatabase::class.java, "sensor_readings").build()
        sensorManager = MySensorManager(getApplication(), arrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE))
    }


    fun startRecording() {
        sensorManager.startSensing()
    }

    fun stopRecording() {
        sensorManager.stopSensing()
    }

    fun deleteRecording(){
        sensorManager.deleteCachedRecordings()
    }

    fun saveRecording(){
        db.sensorReadingsDao().saveAll(sensorManager.getReadings())
    }

}
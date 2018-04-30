package com.example.mac.crossfitcoach.screens.record_session

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.hardware.Sensor
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.Exercise.*
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.dbjava.WorkoutSession
import com.example.mac.crossfitcoach.utils.MySensorManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class RecordExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private var db: SensorDatabase = Room.databaseBuilder(getApplication(),
            SensorDatabase::class.java, "sensor_readings").build()
    private var sensorManager: MySensorManager
    private var currentStep: Int = 0
    private var exerciseStartTime: Date? = null
    private var exerciseEndTime: Date? = null
    private var workoutSteps: Array<WorkoutStep> = arrayOf(
            WorkoutStep(PUSH_UPS),
            WorkoutStep(PULL_UPS),
            WorkoutStep(DOUBLE_UNDERS),
            WorkoutStep(DEAD_LIFT),
            WorkoutStep(BOX_JUMPS),
            WorkoutStep(SQUATS),
            WorkoutStep(CRUNCHES),
            WorkoutStep(KETTLE_BELL_SWINGS))

    init {
        sensorManager = MySensorManager(getApplication(),
                arrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_ROTATION_VECTOR))
    }

    fun startRecording() {
        exerciseStartTime = Calendar.getInstance().time
        sensorManager.startSensing()
    }

    fun getNextWorkoutStep(): WorkoutStep {
        currentStep++
        return workoutSteps.get(currentStep)
    }

    fun isLastStep() = currentStep == workoutSteps.size - 1

    fun getCurrentWorkoutStep(): WorkoutStep {
        return workoutSteps.get(currentStep)
    }

    fun stopRecording() {
        exerciseStartTime = Calendar.getInstance().time
        sensorManager.stopSensing()
    }

    fun deleteRecording() {
        sensorManager.deleteCachedRecordings()
    }

    private var workoutId: Long? = -1

    fun saveRecording(): Completable {
        return Completable.fromAction {
            if (currentStep == 0) {
                workoutId = db.workoutDao().save(WorkoutSession(Calendar.getInstance().time))
            }
            setExerciseId()
            db.sensorReadingsDao().saveAll(sensorManager.getReadings())
        }.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
    }

    private fun setExerciseId() {
        //TODO throw exception
        val exerciseId = db.exerciseDao().save(Exercise(exerciseStartTime, exerciseEndTime, workoutSteps.get(currentStep).exerciseCode, workoutId
                ?: 0))
        for (reading in sensorManager.getReadings()) {
            reading.exerciseId = exerciseId
        }
    }
}
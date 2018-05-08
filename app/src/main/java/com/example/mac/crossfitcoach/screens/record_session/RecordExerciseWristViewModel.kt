package com.example.mac.crossfitcoach.screens.record_session

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.persistence.room.Room
import android.hardware.Sensor
import android.util.Log
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.Exercise.*
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.dbjava.WorkoutSession
import com.example.mac.crossfitcoach.utils.MySensorManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


open class RecordExerciseWristViewModel(application: Application) : AndroidViewModel(application) {

    private var db: SensorDatabase = Room.databaseBuilder(getApplication(),
            SensorDatabase::class.java, "sensor_readings").build()
    private var sensorManager: MySensorManager
    private var currentStep: Int = 0
    private var workoutSteps: Array<WorkoutStep> = arrayOf(
            WorkoutStep(PUSH_UPS),
            WorkoutStep(PULL_UPS),
            WorkoutStep(BURPEES),
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
        workoutSteps.get(currentStep).startTime = Calendar.getInstance().time
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
        workoutSteps.get(currentStep).endTime = Calendar.getInstance().time
        workoutSteps.get(currentStep).readings = sensorManager.getReadings().toMutableList()
        sensorManager.stopSensing()
    }

    fun deleteRecording() {
        sensorManager.deleteCachedRecordings()
    }

    private var workoutId: Long? = -1

    fun saveRecording(): Completable {
        val workoutStep = workoutSteps[currentStep]
        return Completable.fromAction {
            if (workoutSteps.indexOf(workoutStep) == 0) {
                workoutId = db.workoutDao().save(WorkoutSession(Calendar.getInstance().time))
            }
            setExerciseId(workoutStep)
            db.sensorReadingsDao().saveAll(workoutStep.readings)
        }.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .doOnError { Log.d("Andrea", "error " + it.message) }
                .doOnComplete { Log.d("Andrea", "completed") }
    }

    private fun setExerciseId(workoutStep: WorkoutStep) {
        val exerciseId = db.exerciseDao().save(Exercise(workoutStep.startTime, workoutStep.endTime, workoutStep.exerciseCode, workoutId
                ?: 0))
        for (reading in workoutStep.readings!!) {
            reading.exerciseId = exerciseId
        }
    }
}
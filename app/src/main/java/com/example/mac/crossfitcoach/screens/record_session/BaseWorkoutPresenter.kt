package com.example.mac.crossfitcoach.screens.record_session

import android.arch.persistence.room.Room
import android.content.Context
import android.hardware.Sensor
import android.util.Log
import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.DbExercise.*
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.dbjava.WorkoutSession
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutView
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.example.mac.crossfitcoach.utils.MySensorManager
import com.instacart.library.truetime.TrueTime
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

open class BaseWorkoutPresenter(val context: Context, val view: IWorkoutView) : IWorkoutPresenter {

    private var db: SensorDatabase = Room.databaseBuilder(context,
            SensorDatabase::class.java, "sensor_readings").build()
    protected var sensorManager: MySensorManager = MySensorManager(context,
            arrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_ROTATION_VECTOR))
    private var workoutCompleted = false;
    private var exercises: Array<Exercise> = arrayOf(
            Exercise(PUSH_UPS),
//            Exercise(PULL_UPS),
//            Exercise(BURPEES),
//            Exercise(DEAD_LIFT),
//            Exercise(BOX_JUMPS),
//            Exercise(SQUATS),
//            Exercise(CRUNCHES),
            Exercise(KETTLE_BELL_SWINGS))


    private var current: Exercise = exercises[0]
    private var currentExerciseIndex: Int = 0
    protected var workoutId: Long? = -1

    init {
        TrueTimeRx.build()
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .subscribe({ date -> Log.v("Andrea", "TrueTime was initialized and we have a time: $date") }) { throwable -> throwable.printStackTrace() }
    }

    override fun onWorkoutInterrupted() {
        if (!workoutCompleted) {
            if (current.state == Exercise.State.RECORDING) {
                sensorManager.stopSensing()
                sensorManager.deleteCachedRecordings()
            }
            setWorkoutCompleted(false)
        }
    }

    private fun setWorkoutCompleted(completed: Boolean) {
        Completable.fromAction {
            if (currentExerciseIndex > 0) {
                db.workoutDao().setWorkoutCompleted(workoutId!!, completed)
            }
        }.subscribeOn(Schedulers.newThread())
                .subscribe()
    }


    override fun getCurrentExercise(): Exercise {
        return current
    }

    protected fun onStartStopCommand(time: Date) {
        if (current.state == Exercise.State.RECORDING) {
            stopRecording(time)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                    }
            current.state = Exercise.State.STOPPED
            view.updateView(current)
        } else if (current.state == Exercise.State.START) {
            startRecording(time)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        current.state = Exercise.State.RECORDING
                        view.updateView(current)
                    }
        }
    }

    protected fun saveRecordingCommand(repCount: Int) {
        saveRecording(repCount).subscribe()
        if (currentExerciseIndex == exercises.size - 1) {
            workoutCompleted = true
            setWorkoutCompleted(true)
            view.finishWorkout()
        } else {
            currentExerciseIndex += 1
            current = exercises[currentExerciseIndex]
            view.updateView(current)
        }
    }

    fun discarRecordingCommand() {
        discardRecording()
        current.state = Exercise.State.START
        view.updateView(current)
    }

    private fun startRecording(startTime: Date): Completable {
        val now = TrueTime.now()
        val difference = startTime.time - now.time
        return Completable.timer(difference, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .andThen {
                    exercises[currentExerciseIndex].startTime = startTime
                    sensorManager.startSensing(exercises[currentExerciseIndex].exerciseCode)
                    it.onComplete()
                }
    }

    private fun stopRecording(stopTime: Date): Completable {
        val now = TrueTime.now()
        val difference = stopTime.time - now.time
        return Completable.timer(difference, TimeUnit.MILLISECONDS)
                .doOnComplete {
                    sensorManager.stopSensing()
                    exercises[currentExerciseIndex].endTime = stopTime
                    exercises[currentExerciseIndex].readings = sensorManager.getReadings().toMutableList()
                }
    }

    protected fun discardRecording() {
        sensorManager.deleteCachedRecordings()
    }

    private fun saveRecording(repCount: Int): Completable {
        val exercise = exercises[currentExerciseIndex]
        return Completable.fromAction {
            if (exercises.indexOf(exercise) == 0) {
                workoutId = db.workoutDao().save(WorkoutSession(TrueTime.now()))
                Completable.fromAction {
                    view.setWorkoutIdText(workoutId!!)
                }.subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe()
            }
            setExerciseId(exercise)
            db.sensorReadingsDao().saveAll(exercise.readings!!.filter { it.rep <= repCount })
        }.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .doOnError { Log.d("Andrea", "error " + it.message) }
                .doOnComplete { Log.d("Andrea", "completed") }
    }

    private fun setExerciseId(exercise: Exercise) {
        val exerciseId = db.exerciseDao().save(DbExercise(exercise.startTime, exercise.endTime, exercise.exerciseCode, workoutId
                ?: 0))
        for (reading in exercise.readings!!) {
            reading.exerciseId = exerciseId
        }
    }
}
package com.example.mac.crossfitcoach.screens.main

import android.Manifest
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.screens.ble_list.BleClientDeviceListActivity
import com.example.mac.crossfitcoach.screens.exercise_list_duration.ExerciseDurationsActivity
import com.example.mac.crossfitcoach.screens.input_name.InputNameActivity
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.example.mac.crossfitcoach.utils.SharedPreferencesHelper
import com.example.mac.crossfitcoach.utils.checkIfClockIsSynched
import com.example.mac.crossfitcoach.utils.runOnMainThred
import com.example.mac.crossfitcoach.utils.synchClock
import com.instacart.library.truetime.TrueTime
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainMenuActivity : WearableActivity(), StringRecyclerAdapter.OnListItemClicked {

    private lateinit var emojis: Array<String>
    private val strings = arrayOf("Paced Workout", "Free workout", "Connect to Ankle Sensor", "Synch Clock", "Set Reps Duration", "Delete Database")

    override fun onItemListClicked(index: Int) {
        when (index) {
            0, 1 -> {
                if (index == 1) {
                    BaseWorkoutPresenter.exercises = mutableListOf(Exercise(DbExercise.SPEED_WORKOUT), Exercise(DbExercise.EXECUTION_WORKOUT));
                } else {
                    BaseWorkoutPresenter.exercises = arrayOf(
                            Exercise(DbExercise.PUSH_UPS),
                            Exercise(DbExercise.PULL_UPS),
                            Exercise(DbExercise.BURPEES),
                            Exercise(DbExercise.DEAD_LIFT),
                            Exercise(DbExercise.BOX_JUMPS),
                            Exercise(DbExercise.SQUATS),
                            Exercise(DbExercise.CRUNCHES),
                            Exercise(DbExercise.WALL_BALLS),
                            Exercise(DbExercise.KETTLEBELL_PRESS),
                            Exercise(DbExercise.KETTLEBELL_SQUAT_PRESS)
                    ).toMutableList()
                }
                if (!TrueTime.isInitialized()) {
                    runOnMainThred {
                        Toast.makeText(this, "Clock not synched", Toast.LENGTH_LONG).show()
                    }
                    return
                }
                if ((application as MyApplication).bleClient.isConnected) {
                    val i = Intent(this, InputNameActivity::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(this, BleClientDeviceListActivity::class.java)
                    startActivity(i)
                }
            }
            4 -> {
                val i = Intent(this, ExerciseDurationsActivity::class.java)
                startActivity(i)
            }
            5 -> {
                val db = Room.databaseBuilder(getApplication(),
                        SensorDatabase::class.java, "sensor_readings").build()
                Completable.fromAction {
                    db.workoutDao().nukeTable()
                }.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                {
                                    Toast.makeText(this, "Database Deleted!", Toast.LENGTH_LONG).show()
                                },
                                {
                                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                                }
                        )
            }
            2 -> {
                val i = Intent(this, BleClientDeviceListActivity::class.java)
                startActivity(i)
            }
            3 -> {
                checkIfClockIsSynched(this)
            }
        }
    }

    private lateinit var stringAdapter: StringRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        emojis = arrayOf(getString(R.string.emoji_workout), getString(R.string.emoji_workout), getString(R.string.emoji_ankle), getString(R.string.emoji_clock), getString(R.string.emoji_timer), getString(R.string.emoji_bomb))
        initRecyclerView()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 10)
    }

    private fun printSensors() {
        val oSM = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorsList = oSM.getSensorList(Sensor.TYPE_ALL)
        for (s in sensorsList) {
            Log.d("Andrea", (s.toString() + "\n"))
        }
    }

    private fun initRecyclerView() {
        recycler_view.isEdgeItemsCenteringEnabled = true
        recycler_view.layoutManager = WearableLinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.isEdgeItemsCenteringEnabled = true
        stringAdapter = StringRecyclerAdapter(emojis,
                strings, this)
        recycler_view.adapter = stringAdapter
    }
}
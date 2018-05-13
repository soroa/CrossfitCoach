package com.example.mac.crossfitcoach.screens.main

import android.Manifest
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.mac.crossfitcoach.screens.ble_list.BleClientDeviceListActivity
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.instacart.library.truetime.TrueTime
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.concurrent.TimeUnit


class MainMenuActivity : WearableActivity(), StringRecyclerAdapter.OnListItemClicked {

    private lateinit var emojis: Array<String>
    private val strings = arrayOf("Start Workout", "Delete Database", "Connect to Ankle Sensor", "Send toc")

    override fun onItemListClicked(index: Int) {
        when (index) {
            0 -> {
                val i = Intent(this, BleClientDeviceListActivity::class.java)
//                val i = Intent(this, WorkoutActivity::class.java)
                startActivity(i)
            }
            1 -> {
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
                TrueTimeRx.build()
                        .initializeRx("time.google.com")
                        .subscribeOn(Schedulers.io())
                        .subscribe({ date -> Log.v("Andrea", "TrueTime was initialized and we have a time: ${date.time}") }) { throwable -> throwable.printStackTrace() }

//                var timer = Observable.interval(0, 2, TimeUnit.SECONDS)
//                        .subscribe {
//                            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//                            // Vibrate for 500 milliseconds
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
//                            }
//                        }


            }
        }
    }

    private lateinit var stringAdapter: StringRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emojis = arrayOf(getString(R.string.emoji_workout), getString(R.string.emoji_bomb), getString(R.string.emoji_ankle), getString(R.string.emoji_toc))
        setAmbientEnabled()
        initRecyclerView()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 10)

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE NOT SUPPORTED", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "BLE SUPPORTED", Toast.LENGTH_SHORT).show()

        }
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
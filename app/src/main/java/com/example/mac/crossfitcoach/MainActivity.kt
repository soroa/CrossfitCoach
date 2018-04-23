package com.example.mac.crossfitcoach

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.example.mac.crossfitcoach.exercise_list.ExerciseListActivity
import com.example.mac.crossfitcoach.log.LogActivity
import com.example.mac.crossfitcoach.record_session.RecordExerciseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()
//        getInfoAboutSensors()
        data_collect_btn.setOnClickListener { _ ->
            val i = Intent(this, ExerciseListActivity::class.java)
            startActivity(i)
        }

        sensor_log_btn.setOnClickListener { _ ->
            val i = Intent(this, LogActivity::class.java)
            startActivity(i)
        }
        test.setOnClickListener { _ ->
            val i = Intent(this, RecordExerciseActivity::class.java)
            startActivity(i)
        }

    }

    private fun getInfoAboutSensors() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        for (sensor in sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            Log.d("Andrea", "Name: " + sensor.name + "\n"
                    + "Resolution: " + sensor.resolution + "\n"
                    + "min_ delay: " + sensor.minDelay + "\n"
                    + "Type : " + sensor.type + "\n" + "\n")
        }
    }
}
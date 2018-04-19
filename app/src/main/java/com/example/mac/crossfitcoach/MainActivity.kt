package com.example.mac.crossfitcoach

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.view_sensor_log.*


class MainActivity : WearableActivity(), SensorEventListener {
    var accReadingsCount = 0;
    var rotReadingsCount = 0;
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (sensorEvent!!.values!!.size >= 3) {
                    x?.text = String.format("Acc x: %s", sensorEvent.values?.get(0).toString())
                    y?.text = String.format("Acc y: %s", sensorEvent.values?.get(0).toString())
                    z?.text = String.format("Acc z: %s", sensorEvent.values?.get(0).toString())
                }
                accReadingsCount++;
                count_acc.text = Integer.toString(accReadingsCount)
            }

            Sensor.TYPE_GYROSCOPE -> {
                if (sensorEvent!!.values!!.size >= 3) {
                    rot_x?.text = String.format("Rot x: %s", sensorEvent.values?.get(0).toString())
                    rot_y?.text = String.format("Rot y: %s", sensorEvent.values?.get(1).toString())
                    rot_z?.text = String.format("Rot z: %s", sensorEvent.values?.get(2).toString())
                }
                rotReadingsCount++;
                count_rot.text = Integer.toString(rotReadingsCount)
            }

        }
    }

    var sensorManager: SensorManager? = null
    var accelerationSensor: Sensor? = null
    var rotSensor: Sensor? = null
    var x: TextView? = null
    var y: TextView? = null
    var z: TextView? = null
    var rot_x: TextView? = null
    var rot_y: TextView? = null
    var rot_z: TextView? = null
    var acc_count: TextView? = null
    var rot_count: TextView? = null
    var stopButton: Button?=null;
    var sensorsActive = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_sensor_log)
        // Enables Always-on
        setAmbientEnabled()
        initViews()

        getInfoAboutSensors()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        rotSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private fun initViews() {
        x = findViewById(R.id.acc_x)
        y = findViewById(R.id.acc_y)
        z = findViewById(R.id.acc_z)
        rot_x = findViewById(R.id.rot_x)
        rot_y = findViewById(R.id.rot_y)
        rot_z = findViewById(R.id.rot_z)
        acc_count = findViewById(R.id.count_acc)
        rot_count = findViewById(R.id.count_rot)
        stopButton = findViewById(R.id.stop_button)
        stopButton?.setOnClickListener(View.OnClickListener {
            if (sensorsActive) {
                sensorsActive = false
                sensorManager?.unregisterListener(this)
                stopButton?.text = "Start"
                stopButton?.setBackgroundColor(Color.GREEN)
            }else{
                sensorsActive = true
                sensorManager?.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL)
                sensorManager?.registerListener(this, rotSensor, SensorManager.SENSOR_DELAY_NORMAL)
                stopButton?.text = "Stop"
                stopButton?.setBackgroundColor(Color.RED)
            }
        })
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
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

package com.example.mac.crossfitcoach.screens.sensor_placement

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.ankle_connection.AcceptConnectionActivity
import com.example.mac.crossfitcoach.screens.main.MainMenuActivity
import com.example.mac.crossfitcoach.utils.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_sensor_placement.*
import com.example.mac.crossfitcoach.screens.instruction.InstructionActivity

class SensorPlacementActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_placement)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.setIsFirstTime(false)
        wrist.setOnClickListener {
            sharedPreferencesHelper.setSmartwatchPosition(SensorReading.WRIST)
            val i = Intent(this, MainMenuActivity::class.java)
            startActivity(i)
        }
        ankle.setOnClickListener {
            sharedPreferencesHelper.setSmartwatchPosition(SensorReading.ANKLE)
            val i = Intent(this, AcceptConnectionActivity::class.java)
            val extras = Bundle()
            extras.putString(InstructionActivity.INSTRUCTION_TEXT_EXTRA, getString(R.string.put_watch_on_ankle_instruction))
            i.putExtras(extras)
            startActivity(i)
        }
    }
}
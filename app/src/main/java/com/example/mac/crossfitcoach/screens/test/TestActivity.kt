package com.example.mac.crossfitcoach.screens.test

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val bleClient = (application as MyApplication).bleClient

        test_start.setOnClickListener {
            bleClient.sendMsg("Blue")
        }
        test_stop.setOnClickListener {
            bleClient.sendMsg("Start Workout")
        }
    }
}
package com.example.mac.crossfitcoach.screens.workout_done

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_workout_done.*

class WorkoutDoneActivity: WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_done)
        workout_done_container.setOnClickListener {
            finish()
        }
    }
}
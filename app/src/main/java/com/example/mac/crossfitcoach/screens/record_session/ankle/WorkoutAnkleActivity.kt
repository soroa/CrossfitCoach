package com.example.mac.crossfitcoach.screens.record_session.ankle

import android.os.Bundle
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.example.mac.crossfitcoach.screens.record_session.ankle.AnkleWorkoutPresenter
import com.example.mac.crossfitcoach.utils.addTouchEffect
import kotlinx.android.synthetic.main.activity_record_session.*

class WorkoutAnkleActivity : WorkoutActivity() {

    override fun getPresenter(): BaseWorkoutPresenter {
        if (presenter == null) {
        return AnkleWorkoutPresenter(application, this)
        }else{
            return presenter as AnkleWorkoutPresenter
        }
    }

}
package com.example.mac.crossfitcoach.screens.record_session.ankle

import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.example.mac.crossfitcoach.screens.record_session.i.IAnkleWorkoutView

class WorkoutAnkleActivity : WorkoutActivity(), IAnkleWorkoutView {

    override fun connectionStatusChangeed(connected: Boolean) {
        if (!connected) {
            finish()
        }
    }

    override fun finishWorkout() {
        finish()
    }

    override fun interruptWorkout() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        (presenter as AnkleWorkoutPresenter).onViewDestroyed()
    }

    override fun initPresenter(participant:String): BaseWorkoutPresenter {
        if (presenter == null) {
            return AnkleWorkoutPresenter(application, this, participant)
        } else {
            return presenter as AnkleWorkoutPresenter
        }
    }

}
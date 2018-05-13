package com.example.mac.crossfitcoach.screens.record_session.wrist

import android.os.Bundle
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.example.mac.crossfitcoach.screens.record_session.ankle.AnkleWorkoutPresenter
import com.example.mac.crossfitcoach.utils.addTouchEffect
import kotlinx.android.synthetic.main.activity_record_session.*

class WorkoutWristActivity : WorkoutActivity() {

    override fun getPresenter(): BaseWorkoutPresenter {
        return if (presenter == null) {
            WristWorkoutPresenter(application, this)
        }else{
            presenter as WristWorkoutPresenter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        addTouchEffect(record_btn)
        record_btn.setOnClickListener { view ->
            (getPresenter() as WristWorkoutPresenter).onStartStopClicked()
        }
        delete_recording_btn.setOnClickListener {
            (getPresenter() as WristWorkoutPresenter).onDiscarRecordingButtonClicked()
        }
        save_recording_btn.setOnClickListener {
            (getPresenter() as WristWorkoutPresenter).onSaveRecordingClicked()
        }
    }

}
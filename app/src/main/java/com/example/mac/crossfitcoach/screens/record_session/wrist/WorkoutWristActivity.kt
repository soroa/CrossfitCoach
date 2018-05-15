package com.example.mac.crossfitcoach.screens.record_session.wrist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.example.mac.crossfitcoach.utils.addTouchEffect
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_workout.*
import java.util.concurrent.TimeUnit
import android.media.ToneGenerator
import android.media.AudioManager
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.example.mac.crossfitcoach.screens.rep_picker.RepsPickerActivity
import com.example.mac.crossfitcoach.utils.disableTouch
import com.example.mac.crossfitcoach.utils.enableTouch
import io.reactivex.android.schedulers.AndroidSchedulers
import android.app.Activity
import android.widget.Toast
import com.example.mac.crossfitcoach.screens.workout_done.WorkoutDoneActivity
import com.example.mac.crossfitcoach.utils.vibrate


class WorkoutWristActivity : WorkoutActivity() {

    override fun getPresenter(): BaseWorkoutPresenter {
        return if (presenter == null) {
            WristWorkoutPresenter(application, this)
        } else {
            presenter as WristWorkoutPresenter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initButtons()
    }

    override fun finishWorkout() {
        val i = Intent(this, WorkoutDoneActivity::class.java)
        finish()
        startActivity(i)

    }

    private fun initButtons() {
        addTouchEffect(record_btn)
        record_btn.setOnClickListener { view ->
            if (presenter!!.getCurrentExercise().state == Exercise.State.START) {
                startCountdown({ (getPresenter() as WristWorkoutPresenter).onStartStopClicked(5) })
            } else {
                (getPresenter() as WristWorkoutPresenter).onStartStopClicked(1)
            }
        }
        delete_recording_btn.setOnClickListener {
            (getPresenter() as WristWorkoutPresenter).onDiscarRecordingButtonClicked()
        }
        save_recording_btn.setOnClickListener {
            val i = Intent(this, RepsPickerActivity::class.java)
            i.putExtra(RepsPickerActivity.REP_COUNT_EXTRA, (presenter as WristWorkoutPresenter).getMaxRepCountForCurrentExercise())
            startActivityForResult(i, 1)
        }
    }

    override fun connectionStatusChangeed(connected: Boolean) {
        if (!connected) {
            Toast.makeText(this, "Connection Lost! ", Toast.LENGTH_LONG).show()
            vibrate(this, 1000)
            finish()
        }
    }

    private lateinit var countdown: Disposable
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val reps = data!!.getIntExtra("rep_count", 0)
                (getPresenter() as WristWorkoutPresenter).onSaveRecordingClicked(reps)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private fun startCountdown(onSubscribe: () -> Unit) {
        var countdownCounter = -6
        var duration = 100
        //todo make volume 100
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 20)
        disableTouch(workout_container)
        countdown = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe()
                }
                .doOnDispose {
                    enableTouch(workout_container)
                }
                .doOnNext {
                    countdownCounter++
                    session_timer_tv.setTextColor(Color.RED)
                    session_timer_tv.text = countdownCounter.toString()
                    if (countdownCounter == 0) {
                        toneGen1.startTone(ToneGenerator.TONE_DTMF_3, 800)
                        countdown.dispose()
                    } else {
                        toneGen1.startTone(ToneGenerator.TONE_DTMF_3, duration)
                        // Vibrate for 500 milliseconds
                    }
                }.subscribe()
    }
}
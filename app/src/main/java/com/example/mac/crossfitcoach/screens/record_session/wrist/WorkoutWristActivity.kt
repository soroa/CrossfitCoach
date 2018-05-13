package com.example.mac.crossfitcoach.screens.record_session.wrist

import android.graphics.Color
import android.os.Bundle
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.WorkoutActivity
import com.example.mac.crossfitcoach.utils.addTouchEffect
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.concurrent.TimeUnit
import android.media.ToneGenerator
import android.media.AudioManager
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.example.mac.crossfitcoach.utils.disableTouch
import com.example.mac.crossfitcoach.utils.enableTouch
import io.reactivex.android.schedulers.AndroidSchedulers


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
            (getPresenter() as WristWorkoutPresenter).onSaveRecordingClicked()
        }
    }

    private lateinit var countdown: Disposable

    private fun startCountdown(onSubscribe: () -> Unit) {
        var countdownCounter = -5
        var duration = 100
        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        countdown = Observable.interval(1, 1, TimeUnit.SECONDS)
                .doOnSubscribe {
                    session_timer_tv.setTextColor(Color.RED)
                    session_timer_tv.text = countdownCounter.toString()
                    disableTouch(workout_container)
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_3, duration)
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    onSubscribe()
                }
                .doOnDispose {
                    enableTouch(workout_container)
                }
                .doOnNext {
                    countdownCounter++
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
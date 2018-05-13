package com.example.mac.crossfitcoach.screens.record_session

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.util.Log
import android.view.View
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.record_session.ankle.AnkleWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutView
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise
import com.example.mac.crossfitcoach.screens.record_session.wrist.WristWorkoutPresenter
import com.example.mac.crossfitcoach.utils.SharedPreferencesHelper
import com.example.mac.crossfitcoach.utils.addTouchEffect
import com.instacart.library.truetime.TrueTime
import com.instacart.library.truetime.TrueTimeRx
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.concurrent.TimeUnit

abstract class WorkoutActivity : FragmentActivity(), IWorkoutView {

    private var timer: Disposable? = null
    protected var presenter: IWorkoutPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_session)
        AmbientModeSupport.attach(this)
        val smartWatchPos = SharedPreferencesHelper(this).getSmartWatchPosition()
        presenter = getPresenter()
        updateView(presenter!!.getCurrentExercise())
    }

    abstract fun getPresenter(): BaseWorkoutPresenter

    init {
        TrueTimeRx.build()
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .subscribe({ date -> Log.v("Andrea", "TrueTime was initialized and we have a time: $date") }) { throwable -> throwable.printStackTrace() }

    }

    override fun updateView(exercise: Exercise) {
        when (exercise.state) {
            Exercise.State.START -> {
                current_exercise_name_tv.text = exercise.name
                showSaveDeleteButtons(false)
                record_btn.setBackgroundResource(R.drawable.circle_red)
                record_btn.text = getString(R.string.rec)
                session_timer_tv.text = "00:00"
                session_timer_tv.setTextColor(getColor(R.color.black))
            }
            Exercise.State.RECORDING -> {
                showSaveDeleteButtons(false)
                record_btn.setBackgroundResource(R.drawable.square)
                record_btn.text = getString(R.string.stop)
                session_timer_tv.setTextColor(getColor(R.color.red))
                startTimer()
            }
            Exercise.State.STOPPED -> {
                showSaveDeleteButtons(true)
                timer?.dispose()
            }
        }
        if (SharedPreferencesHelper(this).getSmartWatchPosition() == SensorReading.WRIST) {
        }
    }

    private fun showSaveDeleteButtons(show: Boolean) {
        if (show) {
            record_btn.visibility = View.GONE
            delete_recording_btn.visibility = View.VISIBLE
            save_recording_btn.visibility = View.VISIBLE
        } else {
            record_btn.visibility = View.VISIBLE
            delete_recording_btn.visibility = View.GONE
            save_recording_btn.visibility = View.GONE
        }
    }


    private fun startTimer() {
        var startTime = 0L
        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    startTime = TrueTime.now().time
                }
                .subscribe {
                    val timeDif = TrueTime.now().time - startTime
                    val formatted = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(timeDif),
                            TimeUnit.MILLISECONDS.toSeconds(timeDif) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDif))
                    )
                    session_timer_tv.text = formatted
                }
    }

}
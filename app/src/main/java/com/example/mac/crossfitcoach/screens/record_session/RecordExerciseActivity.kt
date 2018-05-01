package com.example.mac.crossfitcoach.screens.record_session

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.view.View
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.screens.workout_done.WorkoutDoneActivity
import com.example.mac.crossfitcoach.utils.CustomViewModelFactory
import com.example.mac.crossfitcoach.utils.INT_VALUE
import com.example.mac.crossfitcoach.utils.RECORD_EXERCISE
import com.example.mac.crossfitcoach.utils.addTouchEffect
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.*
import java.util.concurrent.TimeUnit


class RecordExerciseActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {}
            override fun onExitAmbient() {}
        }
    }

    private lateinit var model: RecordExerciseViewModel
    private var isRecording = false
    private var timer: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_session)
        val myAmbientCallback = AmbientModeSupport.attach(this)
        var args = Bundle()
        args.putInt(INT_VALUE, Exercise.BOX_JUMPS)
        model = ViewModelProviders.of(this, CustomViewModelFactory(RECORD_EXERCISE, args, application)).get(RecordExerciseViewModel::class.java)
        initViews()
    }

    private fun initViews() {
        addTouchEffect(record_btn)
        val exCode = model.getCurrentWorkoutStep().exerciseCode
        current_exercise_name_tv.text = codeToNameExerciseMap.get(exCode)
        reps_tv.text = codeToRepsMap.get(exCode).toString() + " reps"
        record_btn.setOnClickListener { view ->
            if (isRecording) {
                model.stopRecording()
                stopTimer()
                isRecording = false
            } else {
                model.startRecording()
                startTimer()
                isRecording = true
            }
        }

        delete_recording_btn.setOnClickListener {
            session_timer_tv.text = "00:00"
            model.deleteRecording()
            showSaveDeleteButtons(false)
            record_btn.setBackgroundResource(R.drawable.circle_red)
            record_btn.text = "REC"
        }
        save_recording_btn.setOnClickListener {
            model.saveRecording().subscribe(
                    {
                    },
                    {
                        //todo
                    }
            )
            goToNextStep()
        }
    }

    private fun goToNextStep() {
        if (!model.isLastStep()) {
            current_exercise_name_tv.text = codeToNameExerciseMap.get(model.getNextWorkoutStep().exerciseCode)
            session_timer_tv.text = "00:00"
            showSaveDeleteButtons(false)
            record_btn.setBackgroundResource(R.drawable.circle_red)
        } else {
            val i = Intent(this, WorkoutDoneActivity::class.java)
            finish()
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.stopRecording()
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
        record_btn.setBackgroundResource(R.drawable.square)
        record_btn.text = "STOP"
        session_timer_tv.setTextColor(getColor(R.color.red))
        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    startTime = Calendar.getInstance().getTime().time
                }
                .subscribe {
                    val timeDif = Calendar.getInstance().getTime().time - startTime
                    val formatted = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(timeDif),
                            TimeUnit.MILLISECONDS.toSeconds(timeDif) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDif))
                    );
                    session_timer_tv.text = formatted
                }
    }

    private fun stopTimer() {
        session_timer_tv.setTextColor(getColor(R.color.black))
        val myAlertDialog = AlertDialog.Builder(this)
        showSaveDeleteButtons(true)
        timer?.dispose()
    }

    private fun showSaveOrDiscarDialog(myAlertDialog: AlertDialog.Builder) {
        myAlertDialog.setTitle("Do you want to save it? ")
        myAlertDialog.setPositiveButton("YES") { arg0, arg1 ->
            session_timer_tv.text = "00:00"
            model.saveRecording()
            //go to next
        }
        myAlertDialog.setNegativeButton("NO") { arg0, arg1 ->
            session_timer_tv.text = "00:00"
            model.deleteRecording()
        }
        myAlertDialog.show()
    }

}
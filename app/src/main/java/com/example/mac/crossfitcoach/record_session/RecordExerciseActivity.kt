package com.example.mac.crossfitcoach.record_session

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.view.MotionEvent
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.Exercise
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var model: RecordExerciseViewModel
    private var isRecording = false
    private var timer: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_session)
        var args = Bundle()
        args.putInt(INT_VALUE, Exercise.BOX_JUMPS)
        model = ViewModelProviders.of(this, CustomViewModelFactory(RECORD_EXERCISE, args, application)).get(RecordExerciseViewModel::class.java)
        initViews()
    }

    private fun initViews() {
        addTouchEffect(record_btn)
        record_btn.setOnClickListener { view ->
            if (isRecording) {
                stopTimer()
                model.stopRecording()
                isRecording = false
            } else {
                startTimer()
                model.startRecording()
                isRecording = true
            }
        }
    }

    private fun startTimer() {
        var startTime = 0L
        record_btn.setBackgroundResource(R.drawable.square)
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
        val myAlertDialog = AlertDialog.Builder(this)
        showSaveOrDiscarDialog(myAlertDialog)
        record_btn.setBackgroundResource(R.drawable.circle_red)
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
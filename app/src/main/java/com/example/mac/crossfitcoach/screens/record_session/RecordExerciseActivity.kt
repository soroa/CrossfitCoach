package com.example.mac.crossfitcoach.screens.record_session

import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.util.Log
import android.view.View
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_DISCARD_EXERCISE
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_RECORD_BUTTON_CLICK
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_SAVE_EXERCISE
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.workout_done.WorkoutDoneActivity
import com.example.mac.crossfitcoach.utils.*
import com.instacart.library.truetime.TrueTime
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.*
import java.util.concurrent.TimeUnit


class RecordExerciseActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider, BleServer.BleServerEventListener {


    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {}
            override fun onExitAmbient() {}
        }
    }

    private lateinit var modelWrist: RecordExerciseWristViewModel
    private var isRecording = false
    private var timer: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_session)
        AmbientModeSupport.attach(this)
        val args = Bundle()
        args.putInt(INT_VALUE, Exercise.BOX_JUMPS)
        modelWrist = ViewModelProviders.of(this, CustomViewModelFactory(RECORD_EXERCISE, args, application)).get(RecordExerciseWristViewModel::class.java)
        initViews()
        if (SharedPreferencesHelper(this).getSmartWatchPosition() == SensorReading.ANKLE) {
            (application as MyApplication).bleServer.setBleEventListener(this)
        }
    }

    override fun onDeviceConnected(dev: BluetoothDevice) {

    }

    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessageReceived(workoutCommand: WorkoutCommand) {
        when (workoutCommand.command) {
        //todo this button will have the synch timestamp
            BLE_RECORD_BUTTON_CLICK -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = workoutCommand.timestamp!!
                recordButtonClick(calendar.time)
            }
            BLE_SAVE_EXERCISE -> saveRecordingButtonClick()
            BLE_DISCARD_EXERCISE -> deleteRecordingButtonClick()
        }
    }

    private fun initViews() {
        addTouchEffect(record_btn)
        val exCode = modelWrist.getCurrentWorkoutStep().exerciseCode
        current_exercise_name_tv.text = codeToNameExerciseMap.get(exCode)
        reps_tv.text = codeToRepsMap.get(exCode).toString() + " reps"
        if (SharedPreferencesHelper(this).getSmartWatchPosition() == SensorReading.WRIST) {
            initButtons()
        }
    }

    private fun initButtons() {
        record_btn.setOnClickListener { view ->
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND, 1)
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_RECORD_BUTTON_CLICK, cal.timeInMillis))
            recordButtonClick(cal.time)
        }
        delete_recording_btn.setOnClickListener {
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_DISCARD_EXERCISE))
            deleteRecordingButtonClick()
        }
        save_recording_btn.setOnClickListener {
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_SAVE_EXERCISE))
            saveRecordingButtonClick()
        }
    }

    private fun saveRecordingButtonClick() {
        modelWrist.saveRecording().subscribe(
                {

                },
                {
                    //todo
                }
        )
        goToNextStep()
    }

    private fun deleteRecordingButtonClick() {
        session_timer_tv.text = "00:00"
        modelWrist.deleteRecording()
        showSaveDeleteButtons(false)
        record_btn.setBackgroundResource(R.drawable.circle_red)
        record_btn.text = "REC"
    }

    private fun recordButtonClick(time: Date) {
        if (isRecording) {
            modelWrist.stopRecording(time)
            stopTimer()
            isRecording = false
        } else {
            Log.d("Andrea", "Start time:  " + time.time)
            modelWrist.startRecording(time)
            startTimer()
            isRecording = true
        }
    }

    private fun goToNextStep() {
        if (!modelWrist.isLastStep()) {
            current_exercise_name_tv.text = codeToNameExerciseMap.get(modelWrist.getNextWorkoutStep().exerciseCode)
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
        //todo what happens when activity is destroyed??
        modelWrist.stopRecording(TrueTime.now())
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
        record_btn.text = getString(R.string.stop)
        session_timer_tv.setTextColor(getColor(R.color.red))
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

    private fun stopTimer() {
        session_timer_tv.setTextColor(getColor(R.color.black))
        showSaveDeleteButtons(true)
        timer?.dispose()
    }

}
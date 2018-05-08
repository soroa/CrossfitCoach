package com.example.mac.crossfitcoach.screens.record_session

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.view.View
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.*
import com.example.mac.crossfitcoach.dbjava.Exercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.workout_done.WorkoutDoneActivity
import com.example.mac.crossfitcoach.utils.*
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
        var args = Bundle()
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

    override fun onMessageReceived(msg: String) {
        when (msg) {
        //todo this button will have the synch timestamp
            WorkoutCommand.BLE_RECORD_BUTTON_CLICK.toString() -> recordButtonClick()
            WorkoutCommand.BLE_SAVE_EXERCISE.toString() -> saveRecordingButtonClick()
            WorkoutCommand.BLE_DISCARD_EXERCISE.toString() -> deleteRecordingButtonClick()
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
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand.BLE_RECORD_BUTTON_CLICK.toString())
            recordButtonClick()
        }
        delete_recording_btn.setOnClickListener {
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand.BLE_DISCARD_EXERCISE.toString())
            deleteRecordingButtonClick()
        }
        save_recording_btn.setOnClickListener {
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand.BLE_SAVE_EXERCISE.toString())
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

    private fun recordButtonClick() {
        if (isRecording) {
            modelWrist.stopRecording()
            stopTimer()
            isRecording = false
        } else {
            modelWrist.startRecording()
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
        modelWrist.stopRecording()
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
                    startTime = Calendar.getInstance().getTime().time
                }
                .subscribe {
                    val timeDif = Calendar.getInstance().getTime().time - startTime
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

    private fun showSaveOrDiscarDialog(myAlertDialog: AlertDialog.Builder) {
        myAlertDialog.setTitle("Do you want to save it? ")
        myAlertDialog.setPositiveButton("YES") { arg0, arg1 ->
            session_timer_tv.text = "00:00"
            modelWrist.saveRecording()
            //go to next
        }
        myAlertDialog.setNegativeButton("NO") { arg0, arg1 ->
            session_timer_tv.text = "00:00"
            modelWrist.deleteRecording()
        }
        myAlertDialog.show()
    }

}
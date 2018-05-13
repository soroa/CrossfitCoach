package com.example.mac.crossfitcoach.screens.record_session

import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wear.ambient.AmbientModeSupport
import android.support.wearable.view.WearableDialogHelper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleClient
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_DISCARD_EXERCISE
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_RECORD_BUTTON_CLICK
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand.Companion.BLE_SAVE_EXERCISE
import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise.Companion.codeToNameExerciseMap
import com.example.mac.crossfitcoach.screens.record_session.model.Exercise.Companion.codeToRepsMap
import com.example.mac.crossfitcoach.screens.workout_done.WorkoutDoneActivity
import com.example.mac.crossfitcoach.utils.*
import com.instacart.library.truetime.TrueTime
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_session.*
import java.util.*
import java.util.concurrent.TimeUnit


//class RecordExerciseActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider, BleServer.BleServerEventListener, BleClient.BleCommunicationListener {
//
//    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
//        return object : AmbientModeSupport.AmbientCallback() {
//            override fun onEnterAmbient(ambientDetails: Bundle?) {}
//            override fun onExitAmbient() {}
//        }
//    }
//
//    private lateinit var model: RecordExerciseWristViewModel
//    private var isRecording = false
//    private var timer: Disposable? = null
//    private var waitingForResponse: Boolean = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_record_session)
//        AmbientModeSupport.attach(this)
//        val args = Bundle()
//        args.putInt(INT_VALUE, DbExercise.BOX_JUMPS)
//        model = ViewModelProviders.of(this, CustomViewModelFactory(RECORD_EXERCISE, args, application)).get(RecordExerciseWristViewModel::class.java)
//        initViews()
//        if (SharedPreferencesHelper(this).getSmartWatchPosition() == SensorReading.ANKLE) {
//            (application as MyApplication).bleServer.setBleEventListener(this)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        //todo what happens when activity is destroyed??
//        model.stopRecording(TrueTime.now())
//    }
//
//    override fun onDeviceConnected(dev: BluetoothDevice) {
//
//    }
//
//    override fun onDeviceDisconnected(dev: BluetoothDevice) {
//        val dialogBuilder = WearableDialogHelper.DialogBuilder(this)
//        dialogBuilder.setMessage("Sensor got disconnected")
//                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
//                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        finish()
//                    }
//                })
//    }
//
//    override fun onMessageReceived(workoutCommand: WorkoutCommand) {
//        when (workoutCommand.command) {
//            BLE_RECORD_BUTTON_CLICK -> {
//                val calendar = Calendar.getInstance()
//                calendar.timeInMillis = workoutCommand.timestamp!!
//                startStopButtonClick(calendar.time)
//            }
//            BLE_SAVE_EXERCISE -> saveRecordingButtonClick()
//            BLE_DISCARD_EXERCISE -> deleteRecordingButtonClick()
//        }
//    }
//
//    override fun onMessageReturned(msg: WorkoutCommand) {
//        waitingForResponse = false
//    }
//
//    private fun initViews() {
//        addTouchEffect(record_btn)
//        val exCode = model.getCurrentWorkoutStep().exerciseCode
//        current_exercise_name_tv.text = codeToNameExerciseMap.get(exCode)
//        reps_tv.text = codeToRepsMap.get(exCode).toString() + " reps"
//        if (SharedPreferencesHelper(this).getSmartWatchPosition() == SensorReading.WRIST) {
//            initButtons()
//        }
//    }
//
//    private fun initButtons() {
//        record_btn.setOnClickListener { view ->
//            val cal = Calendar.getInstance()
//            cal.add(Calendar.SECOND, 3)
//            startResponseTimer()
//            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_RECORD_BUTTON_CLICK, cal.timeInMillis), this)
//            startStopButtonClick(cal.time)
//        }
//        delete_recording_btn.setOnClickListener {
//            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_DISCARD_EXERCISE), this)
//            deleteRecordingButtonClick()
//        }
//        save_recording_btn.setOnClickListener {
//            waitingForResponse = true
//            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(BLE_SAVE_EXERCISE), this)
//            saveRecordingButtonClick()
//        }
//    }
//
//    private fun startResponseTimer() {
//        waitingForResponse = true
//        Completable.timer(2, TimeUnit.SECONDS).subscribe {
//            if (waitingForResponse) {
//                //error in connection
//                Toast.makeText(this, "Connection timed out", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun saveRecordingButtonClick() {
//        model.saveRecording().subscribe(
//                {
//
//                },
//                {
//                    //todo
//                }
//        )
//        goToNextStep()
//    }
//
//    private fun deleteRecordingButtonClick() {
//        session_timer_tv.text = "00:00"
//        model.deleteRecording()
//        showSaveDeleteButtons(false)
//        record_btn.setBackgroundResource(R.drawable.circle_red)
//        record_btn.text = "REC"
//    }
//
//    private fun startStopButtonClick(time: Date) {
//        if (isRecording) {
//            model.stopRecording(time)
//            stopTimer()
//            isRecording = false
//        } else {
//            Log.d("Andrea", "Start time:  " + time.time)
//            model.startRecording(time, this)
//            startTimer()
//            isRecording = true
//        }
//    }
//    private fun goToNextStep() {
//        if (!model.isLastStep()) {
//            current_exercise_name_tv.text = codeToNameExerciseMap.get(model.getNextWorkoutStep().exerciseCode)
//            session_timer_tv.text = "00:00"
//            showSaveDeleteButtons(false)
//            record_btn.setBackgroundResource(R.drawable.circle_red)
//        } else {
//            val i = Intent(this, WorkoutDoneActivity::class.java)
//            finish()
//            startActivity(i)
//        }
//    }
//
//
//    private fun showSaveDeleteButtons(show: Boolean) {
//        if (show) {
//            record_btn.visibility = View.GONE
//            delete_recording_btn.visibility = View.VISIBLE
//            save_recording_btn.visibility = View.VISIBLE
//        } else {
//            record_btn.visibility = View.VISIBLE
//            delete_recording_btn.visibility = View.GONE
//            save_recording_btn.visibility = View.GONE
//        }
//    }
//
//    private fun startTimer() {
//        var startTime = 0L
//        record_btn.setBackgroundResource(R.drawable.square)
//        record_btn.text = getString(R.string.stop)
//        session_timer_tv.setTextColor(getColor(R.color.red))
//        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    startTime = TrueTime.now().time
//                }
//                .subscribe {
//                    val timeDif = TrueTime.now().time - startTime
//                    val formatted = String.format("%02d:%02d",
//                            TimeUnit.MILLISECONDS.toMinutes(timeDif),
//                            TimeUnit.MILLISECONDS.toSeconds(timeDif) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDif))
//                    )
//                    session_timer_tv.text = formatted
//                }
//    }
//
//    private fun stopTimer() {
//        session_timer_tv.setTextColor(getColor(R.color.black))
//        showSaveDeleteButtons(true)
//        timer?.dispose()
//    }
//}
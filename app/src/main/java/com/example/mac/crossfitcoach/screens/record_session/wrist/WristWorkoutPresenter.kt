package com.example.mac.crossfitcoach.screens.record_session.wrist

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.communication.ble.BleClient
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutView
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutWristPresenter
import com.example.mac.crossfitcoach.utils.vibrate
import com.instacart.library.truetime.TrueTime
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

class WristWorkoutPresenter(app: Application, view: IWorkoutView, participant:String) : BaseWorkoutPresenter(app, view, participant), IWorkoutWristPresenter, BleClient.BleCommunicationListener {

    override fun onDisconected(device: BluetoothDevice) {
        Completable.fromAction {
            Toast.makeText(context, "Connection Lost", Toast.LENGTH_SHORT).show()
            vibrate(context, 1000)
            view.connectionStatusChangeed(false)
        }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun onSaveRecordingClicked(repCount: Int) {
        (context.applicationContext as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_SAVE_EXERCISE, repCount = repCount), this)
        super.saveRecordingCommand(repCount)
    }

    private var waitingForResponse: Boolean = false

    override fun onMessageReturned(msg: WorkoutCommand) {
        Log.d("Andrea", "Message returned " + msg.command)
        waitingForResponse = false
    }

    override fun onStartStopClicked(delay: Int) {
        val truth = TrueTime.now()
        val cal = Calendar.getInstance()
        cal.time = truth
        cal.add(Calendar.SECOND, delay)
        startResponseTimer()
        (context.applicationContext as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_RECORD_BUTTON_CLICK, cal.timeInMillis), this)
        onStartStopCommand(cal.time)
    }


    override fun onDiscarRecordingButtonClicked() {
        (context.applicationContext as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_DISCARD_EXERCISE), this)
        super.discarRecordingCommand()
    }

    fun getMaxRepCountForCurrentExercise(): Int {
        return sensorManager.rep
    }

    override fun onWorkoutInterrupted() {
        super.onWorkoutInterrupted()
        (context.applicationContext as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_END_WORKOUT), this)
    }

    private fun startResponseTimer() {
        waitingForResponse = true
        Completable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (waitingForResponse) {
                        //error in connection
                        Toast.makeText(context, "Connection timed out", Toast.LENGTH_SHORT).show()
                        vibrate(context, 1000)
                    }
                }
    }
}
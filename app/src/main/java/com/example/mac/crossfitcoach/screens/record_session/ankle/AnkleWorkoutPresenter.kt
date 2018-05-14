package com.example.mac.crossfitcoach.screens.record_session.ankle

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.DialogInterface
import android.support.wearable.view.WearableDialogHelper
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IAnkleWorkoutView
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutView
import java.util.*

class AnkleWorkoutPresenter(app: Application, view: IAnkleWorkoutView) : BaseWorkoutPresenter(app, view), BleServer.BleServerEventListener {

    init {
        (app as MyApplication).bleServer.setBleEventListener(this)
    }

    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        view.connectionStatusChangeed(false)

    }

    override fun onMessageReceived(workoutCommand: WorkoutCommand) {
        when (workoutCommand.command) {
            WorkoutCommand.BLE_RECORD_BUTTON_CLICK -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = workoutCommand.timestamp!!
                onStartStopCommand(calendar.time)
            }
            WorkoutCommand.BLE_SAVE_EXERCISE -> saveRecordingCommand(workoutCommand.repCount!!)
            WorkoutCommand.BLE_DISCARD_EXERCISE -> discarRecordingCommand()
            WorkoutCommand.BLE_END_WORKOUT -> (view as IAnkleWorkoutView).interruptWorkout()
        }
    }

    override fun onDeviceConnected(dev: BluetoothDevice) {
    }
}
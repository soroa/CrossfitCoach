package com.example.mac.crossfitcoach.screens.record_session.ankle

import android.app.Application
import android.bluetooth.BluetoothDevice
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import com.example.mac.crossfitcoach.screens.record_session.i.IAnkleWorkoutView
import com.example.mac.crossfitcoach.screens.record_session.i.IWorkoutAnklePresenter
import com.example.mac.crossfitcoach.utils.vibrate
import java.util.*

class AnkleWorkoutPresenter(val app: Application, view: IAnkleWorkoutView, participant:String) : BaseWorkoutPresenter(app, view, participant), BleServer.BleServerEventListener, IWorkoutAnklePresenter {
    override fun onViewDestroyed() {
        (app as MyApplication).bleServer.removeBleEventListener(this)
    }

    init {
        (app as MyApplication).bleServer.setBleEventListener(this)
    }

    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        view.connectionStatusChangeed(false)

    }

    override fun onMessageReceived(workoutCommand: WorkoutCommand) {
        when (workoutCommand.command) {
            WorkoutCommand.BLE_RECORD_BUTTON_CLICK -> {
                vibrate(app, 200)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = workoutCommand.timestamp!!
                onStartStopCommand(calendar.time)
            }
            WorkoutCommand.BLE_SAVE_EXERCISE ->{
                saveRecordingCommand(workoutCommand.repCount!!)
                vibrate(app, 200)
            }

            WorkoutCommand.BLE_DISCARD_EXERCISE -> {
                discarRecordingCommand()
                vibrate(app, 200)
            }
            WorkoutCommand.BLE_END_WORKOUT -> {
                (view as IAnkleWorkoutView).interruptWorkout()
                vibrate(app, 200)

            }
        }
    }

    override fun onDeviceConnected(dev: BluetoothDevice) {
    }
}
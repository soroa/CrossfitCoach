package com.example.mac.crossfitcoach.screens.record_session

import android.app.Application
import android.bluetooth.BluetoothDevice
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.communication.ble.BleServer


class RecordExerciseAnkleViewModel(application: Application) : RecordExerciseWristViewModel(application), BleServer.BleServerEventListener {

    private var bluetoothServer: BleServer

    init {
        bluetoothServer = (application as MyApplication).bleServer
    }


    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessageReceived(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeviceConnected(dev: BluetoothDevice) {
    }
}

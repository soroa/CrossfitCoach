package com.example.mac.crossfitcoach.screens.ankle_connection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.wear.ambient.AmbientModeSupport
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.input_name.InputNameActivity.Companion.PARTICIPANT_NAME
import com.example.mac.crossfitcoach.screens.instruction.InstructionActivity
import com.example.mac.crossfitcoach.screens.record_session.ankle.WorkoutAnkleActivity
import com.example.mac.crossfitcoach.utils.checkIfClockIsSynched
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_message.*


class AcceptConnectionActivity : InstructionActivity(), BleServer.BleServerEventListener, AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var bluetoothServer: BleServer

    override fun onMessageReceived(msg: WorkoutCommand) {
        if (msg.command.equals(WorkoutCommand.BLE_START_WORKOUT)) {
            val i = Intent(this, WorkoutAnkleActivity::class.java)
            i.putExtra(PARTICIPANT_NAME, msg.participant)
            startActivity(i)
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle?) {}
            override fun onExitAmbient() {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothServer = (application as MyApplication).bleServer
        AmbientModeSupport.attach(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            bluetoothServer.startAdvertising()
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfClockIsSynched(this)
        if (!bluetoothServer.isBluetoothOn()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
            finish()
        } else {
            initView()
            bluetoothServer.setBleEventListener(this)
            bluetoothServer.startAdvertising()
        }
    }

    private fun initView() {
        if (bluetoothServer.connectedDevices.size > 0) {
            instruction_text_tv.setText("Devices connected " + bluetoothServer.connectedDevices.size + " \n \n " + bluetoothServer.connectedDevices.get(0)!!.name)
            instruction_container.background = getDrawable(R.color.green)
        } else {
            instruction_text_tv.setText("Devices connected " + bluetoothServer.connectedDevices.size)
            instruction_container.background = getDrawable(R.color.dark_grey)
        }
    }

    override fun onPause() {
        super.onPause()
        bluetoothServer.stopAdvertising()
        bluetoothServer.removeBleEventListener(this)
    }

    override fun onDeviceConnected(dev: BluetoothDevice) {
        Completable.fromAction {
            Toast.makeText(this, "Connected to " + dev.name, Toast.LENGTH_SHORT).show()
            initView()
        }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        Completable.fromAction {
            Toast.makeText(this, "Disconnected from " + dev.name, Toast.LENGTH_SHORT).show()
            initView()
        }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}
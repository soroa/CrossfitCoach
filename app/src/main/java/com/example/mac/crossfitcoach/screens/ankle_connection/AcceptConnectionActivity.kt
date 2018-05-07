package com.example.mac.crossfitcoach.screens.ankle_connection

import android.app.Activity
import android.bluetooth.*
import android.os.Bundle
import com.example.mac.crossfitcoach.screens.instruction.InstructionActivity
import android.content.Intent
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleServer
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_message.*


class AcceptConnectionActivity : InstructionActivity(), BleServer.BleServerEventListener {
    override fun onMessageReceived(msg: String) {
        Toast.makeText(this, "Message received " + msg, Toast.LENGTH_SHORT).show()
    }

    private lateinit var bluetoothServer: BleServer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothServer = (application as MyApplication).bleServer
        bluetoothServer.setBleEventListener(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            bluetoothServer.startAdvertising()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!bluetoothServer.isBluetoothOn()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
            finish()
        } else {
            initView()
                bluetoothServer.startAdvertising()
        }
    }

    private fun initView() {
        if(bluetoothServer.connectedDevices.size>0){
            instruction_text_tv.setText("Devices connected " + bluetoothServer.connectedDevices.size + " \n \n " + bluetoothServer.connectedDevices.get(0)!!.name)
            instruction_container.background = getDrawable(R.color.green)
        }else{
            instruction_text_tv.setText("Devices connected " + bluetoothServer.connectedDevices.size)
            instruction_container.background = getDrawable(R.color.dark_grey)
        }
    }

    override fun onPause() {
        super.onPause()
        bluetoothServer.stopAdvertising()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothServer.stopServer()
    }


    override fun onDeviceConnected(dev: BluetoothDevice) {
        Completable.fromAction {
            Toast.makeText(this, "Connected to " + dev.name, Toast.LENGTH_SHORT).show()
        }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
        initView()
    }

    override fun onDeviceDisconnected(dev: BluetoothDevice) {
        Toast.makeText(this, "Disconnected from " + dev.name, Toast.LENGTH_SHORT).show()
        Completable.fromAction {
            instruction_container.background = getDrawable(R.color.dark_grey)
        }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}
package com.example.mac.crossfitcoach.communication

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.content.BroadcastReceiver





class BluetoothHelper(activity: Activity) {

    private val receiver:BroadcastReceiver;
    private val mBluetoothAdapter:BluetoothAdapter
    var isScanning: Boolean = false

    init {

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val action = intent.action
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                }
            }
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity.registerReceiver(receiver, filter)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun startDiscovery(){
        mBluetoothAdapter.startDiscovery()
    }


    fun quereyPairedDevices(){
        val pairedDevices = mBluetoothAdapter.getBondedDevices()
        if (pairedDevices.size > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (device in pairedDevices) {
                val deviceName = device.getName()
                val deviceHardwareAddress = device.getAddress() // MAC address
            }
        }
    }



}
package com.example.mac.crossfitcoach.screens.ble_list

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleClient
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.record_session.RecordExerciseActivity
import kotlinx.android.synthetic.main.activity_ble_devices_list.*

class BleClientDeviceListActivity : WearableActivity(), BleClient.BleClientEventListener, BleDevicesRecyclerAdapter.OnBleDeviceClicked {


    override fun onServiceFound() {
        (application as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_START_WORKOUT))
        val i = Intent(this, RecordExerciseActivity::class.java)
        startActivity(i)
    }

    private lateinit var bleDevicesAdapter: BleDevicesRecyclerAdapter
    private var devices: MutableList<BluetoothDevice> = arrayListOf()

    override fun onConnectionAccepted(device: BluetoothDevice) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionFailed(device: BluetoothDevice) {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
    }

    override fun onDisconected(device: BluetoothDevice) {
        Toast.makeText(this, "Disconnect", Toast.LENGTH_SHORT).show()
    }


    override fun onBleDeviceClicked(device: BluetoothDevice) {
        (application as MyApplication).bleClient.scanLeDevices(false)
        (application as MyApplication).bleClient.connectDevice(device)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_devices_list)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        (application as MyApplication).bleClient.setBleEventListener(this)
        (application as MyApplication).bleClient.scanLeDevices(
                true)
    }

    override fun onPause() {
        super.onPause()
        (application as MyApplication).bleClient.scanLeDevices(false)
        (application as MyApplication).bleClient.removeBleEventListener(this)
    }

    override fun onScanResult(device: BluetoothDevice) {
        if (!devices.contains(device)) {
            devices.add(device)
            bleDevicesAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        recycler_view.isEdgeItemsCenteringEnabled = true
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.isEdgeItemsCenteringEnabled = true
        bleDevicesAdapter = BleDevicesRecyclerAdapter(devices, this)
        recycler_view.adapter = bleDevicesAdapter
    }

}
package com.example.mac.crossfitcoach.screens.ankle_connection

import android.os.Bundle
import com.example.mac.crossfitcoach.screens.instruction.InstructionActivity
import android.bluetooth.BluetoothAdapter
import android.content.Intent



class AcceptConnectionActivity: InstructionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1800)
        startActivity(discoverableIntent)
    }
}
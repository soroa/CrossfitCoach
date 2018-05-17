package com.example.mac.crossfitcoach.screens.ankle_connection

import android.app.Activity
import android.arch.persistence.room.Room
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.wear.ambient.AmbientModeSupport
import android.view.View
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.BleServer
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.screens.input_name.InputNameActivity.Companion.PARTICIPANT_NAME
import com.example.mac.crossfitcoach.screens.instruction.InstructionActivity
import com.example.mac.crossfitcoach.screens.record_session.ankle.WorkoutAnkleActivity
import com.example.mac.crossfitcoach.utils.addTouchEffect
import com.example.mac.crossfitcoach.utils.checkIfClockIsSynched
import com.example.mac.crossfitcoach.utils.synchClock
import com.example.mac.crossfitcoach.utils.vibrate
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_accept_connection.*


class AcceptConnectionActivity : InstructionActivity(), BleServer.BleServerEventListener, AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var bluetoothServer: BleServer


    override fun onMessageReceived(msg: WorkoutCommand) {
        if (msg.command.equals(WorkoutCommand.BLE_START_WORKOUT)) {
            val i = Intent(this, WorkoutAnkleActivity::class.java)
            vibrate(this,200)
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
        delete_db_button.setOnClickListener {
            val db = Room.databaseBuilder(getApplication(),
                    SensorDatabase::class.java, "sensor_readings").build()
            Completable.fromAction {
                db.workoutDao().nukeTable()
            }.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                            {
                                Toast.makeText(this, "Database Deleted!", Toast.LENGTH_LONG).show()
                            },
                            {
                                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                            }
                    )
        }
        synch_clock.setOnClickListener {
            synchClock(this)
        }
        show_settings.setOnClickListener {
            if (delete_db_button.visibility == View.VISIBLE) {
                delete_db_button.visibility = View.GONE
                synch_clock.visibility = View.GONE
            } else {
                delete_db_button.visibility = View.VISIBLE
                synch_clock.visibility = View.VISIBLE
            }
        }
        addTouchEffect(delete_db_button)
        addTouchEffect(synch_clock)
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
        if (bluetoothServer.isConnectedToWatch()) {
            instruction_text_tv.setText("Connected")
            instruction_container.background = getDrawable(R.color.green)
        } else {
            instruction_text_tv.setText("Not connected")
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
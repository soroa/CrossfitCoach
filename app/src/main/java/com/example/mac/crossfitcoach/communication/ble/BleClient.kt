package com.example.mac.crossfitcoach.communication.ble

import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log
import android.bluetooth.BluetoothGattCharacteristic
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.UnsupportedEncodingException
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import java.util.*


class BleClient(val context: Context) : BleEndPoint<BleClient.BleClientEventListener>() {

    private var mGatt: BluetoothGatt? = null
    var mConnected: Boolean = false
    private var mInitialized: Boolean = false
    private var BLEAdapter: BluetoothAdapter
    private var scanner: BluetoothLeScanner
    private var mHandler = Handler()
    var connectedDevice: BluetoothDevice? = null
    var messageLeavingTime: Date? = null

    private var scanning = false

    init {
        val bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        BLEAdapter = bluetoothManager.adapter
        scanner = BLEAdapter.bluetoothLeScanner
    }

    fun scanLeDevices(enable: Boolean) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed({
                scanning = false
                scanner.startScan(object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult?) {
                        super.onScanResult(callbackType, result)
                        for (l in listeners) l.onScanResult(result!!.device)
                    }
                })
            }, 1000)
            scanning = true
        } else {
            scanning = false
            scanner.stopScan(object : ScanCallback() {
            })
        }
    }

    fun getDevice(): BluetoothDevice? {
        return mGatt?.device
    }

    fun pauseScanning() {
        if (scanning && BLEAdapter.isEnabled()) {
            scanner.stopScan(object : ScanCallback() {})
        }
        scanning = false
        mHandler = Handler()
    }

    fun disconnectGattServer() {
        mConnected = false
        if (mGatt != null) {
            mGatt?.disconnect()
            mGatt?.close()
        }
    }

    fun connectDevice(device: BluetoothDevice) {
        if (connectedDevice != device) {
            disconnectGattServer()
        }
        val gattClientCallback = GattClientCallback()
        mGatt = device.connectGatt(context, false, gattClientCallback)
    }

    private inner class GattClientCallback : BluetoothGattCallback() {
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.d("Andrea", "service discovered")
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d("Andrea", "service discovered but not success")
                return
            }
            val service = gatt?.getService(BleServer.uuid) ?: return
            val characteristic = service.getCharacteristic(BleServer.characteristicUUID)
            characteristic?.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
            mInitialized = gatt.setCharacteristicNotification(characteristic, true)
            Log.d("Andrea", "service initilizaed " + mInitialized)
            if (mInitialized) {
                for (l in listeners) l.onServiceFound()
            } else {
                disconnectGattServer()
                Log.d("Andrea", "service initilizaed " + mInitialized)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            //todo remove arrivatl time shizzle
            val arrivalTime = Calendar.getInstance().time.time
            val dif = arrivalTime - messageLeavingTime!!.time
            Log.d("Andrea", "Roundtrip time in ms: " + dif)
            val message = Gson().fromJson(String(characteristic?.value!!), WorkoutCommand::class.java)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("Andrea", "connection worked: " + gatt!!.device.name)
                mConnected = true
                connectedDevice = gatt.device
                discoverServices()
                Completable.fromAction {
                    for (l in listeners) l.onConnectionAccepted(gatt!!.device)
                }.subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()

            } else {
                connectedDevice = null
                for (l in listeners) l.onConnectionFailed(gatt!!.device)
                mConnected = false
                Log.d("Andrea", "state disconnected")
                for (l in listeners) l.onDisconected(gatt!!.device)
                disconnectGattServer()
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            val messageBytes = characteristic!!.getValue()
            var messageString: String? = null
            try {
                messageString = String(messageBytes)
            } catch (e: UnsupportedEncodingException) {
                Log.e("Andrea", "Unable to convert message bytes to string")
            }

            Log.d("Andrea", "Received message: " + messageString!!)
        }
    }

    fun discoverServices() {
        Completable.timer(2, TimeUnit.SECONDS).subscribe {
            mGatt!!.discoverServices()
        }
    }

    fun sendMsg(command: WorkoutCommand) {
        if (!mConnected) {
            return
        }
        val service = mGatt!!.getService(BleServer.uuid)
        val characteristic = service.getCharacteristic(BleServer.characteristicUUID)
        var messageBytes = ByteArray(0)
        val message = Gson().toJson(command)
        try {
            messageBytes = message.toByteArray()
        } catch (e: UnsupportedEncodingException) {
            Log.e("Andrea", "Failed to convert message string to byte array")
        }
        characteristic.value = messageBytes
        val success = mGatt!!.writeCharacteristic(characteristic)
        messageLeavingTime = Calendar.getInstance().time
    }

    interface BleClientEventListener : BleEventListener {
        fun onScanResult(device: BluetoothDevice)
        fun onConnectionAccepted(device: BluetoothDevice)
        fun onConnectionFailed(device: BluetoothDevice)
        fun onDisconected(device: BluetoothDevice)
        fun onServiceFound()
    }

}
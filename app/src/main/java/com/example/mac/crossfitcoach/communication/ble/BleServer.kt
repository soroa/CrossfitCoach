package com.example.mac.crossfitcoach.communication.ble

import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.support.wearable.activity.WearableActivity
import android.util.Log
import java.util.*
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothDevice
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers


class BleServer(val context: Context) : BleEndPoint<BleServer.BleServerEventListener>() {
    private var mBluetoothManager: BluetoothManager = context.getSystemService(WearableActivity.BLUETOOTH_SERVICE) as BluetoothManager
    private var mBluetoothAdapter: BluetoothAdapter
    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null

    companion object {
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        val characteristicUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    var connectedDevices: ArrayList<BluetoothDevice?> = ArrayList()

    private val mAdvertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Log.d("Andrea", "Peripheral advertising started.")
        }

        override fun onStartFailure(errorCode: Int) {
            Log.d("Andrea", "Peripheral advertising failed: $errorCode")
        }
    }

    private var mGattServer: BluetoothGattServer? = null

    init {
        mBluetoothAdapter = mBluetoothManager.adapter
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser()
        val gattServerCallback = GattServerCallback()
        mGattServer = mBluetoothManager.openGattServer(context, gattServerCallback)
        setupServer()
    }

    private fun setupServer() {
        val service = BluetoothGattService(uuid,
                BluetoothGattService.SERVICE_TYPE_PRIMARY)
        val writeCharacteristic = BluetoothGattCharacteristic(
                characteristicUUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE)
        service.addCharacteristic(writeCharacteristic)
        mGattServer!!.addService(service)
    }

    fun stopServer() {
        mGattServer?.close()
        connectedDevices.clear()
    }

    fun isBluetoothOn(): Boolean {
        return (mBluetoothAdapter.isEnabled)
    }


    fun startAdvertising() {
        Log.d("Andrea", "Start Advertising function")
        if (mBluetoothLeAdvertiser == null) {
            return
        }
        val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
                .build()

        val parcelUuid = ParcelUuid(uuid)
        val data = AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(parcelUuid)
                .build()
        mBluetoothLeAdvertiser!!.startAdvertising(settings, data, mAdvertiseCallback);
    }

    fun stopAdvertising() {
        mBluetoothLeAdvertiser?.stopAdvertising(mAdvertiseCallback)
    }

    private inner class GattServerCallback : BluetoothGattServerCallback() {
        
        override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
            super.onConnectionStateChange(device, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("Andrea", "connection connected on server")
                for (l in listeners) l.onDeviceConnected(device!!)
                if (!connectedDevices.contains(device)) {
                    connectedDevices.add(device)
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("Andrea", "connection disconnected on server")
                for (l in listeners) l.onDeviceDisconnected(device!!)
                connectedDevices.remove(device)
            }
        }

        override fun onCharacteristicWriteRequest(device: BluetoothDevice?, requestId: Int, characteristic: BluetoothGattCharacteristic?, preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray?) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value)
            if (characteristic?.getUuid()!!.equals(characteristicUUID)) {
                mGattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
                val message = String(value!!)
                Log.d("Andrea", "Message " + message)
                Completable.fromAction {
                    for (l in listeners) l.onMessageReceived(message)
                }
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()
                val length = value!!.size
                val reversed = ByteArray(length)
                for (i in 0 until length) {
                    reversed[i] = value[length - (i + 1)]
                }
                characteristic!!.setValue(reversed)
                mGattServer!!.notifyCharacteristicChanged(device, characteristic, false)
            }
        }
    }

    interface BleServerEventListener : BleEndPoint.BleEventListener {
        fun onDeviceConnected(dev: BluetoothDevice)
        fun onDeviceDisconnected(dev: BluetoothDevice)
        fun onMessageReceived(msg: String)

    }
}
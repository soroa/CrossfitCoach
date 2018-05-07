package com.example.mac.crossfitcoach.communication.ble

abstract class BleEndPoint<T:BleEndPoint.BleEventListener> {

    var listeners: MutableList<T> = mutableListOf()

    fun setBleEventListener(listener: T) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeBleEventListener(listener: T) {
        listeners.remove(listener)
    }

    interface BleEventListener
}
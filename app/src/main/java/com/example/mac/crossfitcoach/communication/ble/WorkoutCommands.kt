package com.example.mac.crossfitcoach.communication.ble


enum class WorkoutCommand(private val str: String) {

    BLE_RECORD_BUTTON_CLICK("BLE_RECORD_BUTTON_CLICK"),
    BLE_DISCARD_EXERCISE("COMMAND_DISCARD_EXERCISE"),
    BLE_SAVE_EXERCISE("COMMAND_SAVE_EXERCISE"),
    BLE_START_WORKOUT("COMMAND_START_WORKOUT");

    override fun toString(): String {
        return str
    }


}

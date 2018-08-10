package com.example.mac.crossfitcoach.communication.ble


data class WorkoutCommand(
        val command: String,
        val timestamp: Long? = null,
        val repCount: Int? = null,
        val participant: String? = null,
        val exercises: Array<Int>? = null,
        val repsDurations: Array<Int>?= null
) {
    companion object {
        val BLE_RECORD_BUTTON_CLICK = "BLE_RECORD_BUTTON_CLICK"
        val BLE_DISCARD_EXERCISE = "COMMAND_DISCARD_EXERCISE"
        val BLE_SAVE_EXERCISE = "COMMAND_SAVE_EXERCISE"
        val BLE_START_WORKOUT = "COMMAND_START_WORKOUT"
        val BLE_END_WORKOUT = "COMMAND_END_WORKOUT"
    }
}
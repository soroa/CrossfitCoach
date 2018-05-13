package com.example.mac.crossfitcoach.screens.record_session.model

import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class Exercise(var exerciseCode: Int,
               var startTime: Date? = null,
               var endTime: Date? = null,
               var readings: List<SensorReading>? = null,
               val avgRepduration: Long = 0,
               var name: String? = codeToNameExerciseMap[exerciseCode]) {

    var state: State = State.START

    enum class State {
        START, RECORDING, STOPPED
    }

    companion object {
        val codeToNameExerciseMap = mapOf(DbExercise.DEAD_LIFT to "Dead Lift",
                DbExercise.PUSH_UPS to "Push ups",
                DbExercise.PULL_UPS to "Pull ups",
                DbExercise.BURPEES to "Burpees",
                DbExercise.SQUATS to "Squats",
                DbExercise.BOX_JUMPS to "Box jumps",
                DbExercise.KETTLE_BELL_SWINGS to "Kettle B swings",
                DbExercise.DEAD_LIFT to "Dead lifts",
                DbExercise.THRUSTERS to "Thrusters",
                DbExercise.CRUNCHES to "Crunches",
                DbExercise.SINGLE_UNDERS to "Crunches",
                DbExercise.DOUBLE_UNDERS to "Double unders")

        val codeToRepsMap = mapOf(DbExercise.DEAD_LIFT to 5,
                DbExercise.PUSH_UPS to 5,
                DbExercise.PULL_UPS to 5,
                DbExercise.BURPEES to 5,
                DbExercise.SQUATS to 5,
                DbExercise.BOX_JUMPS to 5,
                DbExercise.KETTLE_BELL_SWINGS to 5,
                DbExercise.DEAD_LIFT to 5,
                DbExercise.THRUSTERS to 5,
                DbExercise.CRUNCHES to 5,
                DbExercise.DOUBLE_UNDERS to 5,
                DbExercise.SINGLE_UNDERS to 5)

    }

}


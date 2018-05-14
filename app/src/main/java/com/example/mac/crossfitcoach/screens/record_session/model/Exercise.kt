package com.example.mac.crossfitcoach.screens.record_session.model

import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class Exercise(var exerciseCode: Int,
               var startTime: Date? = null,
               var endTime: Date? = null,
               var readings: MutableList<SensorReading>? = null,
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

        val codeToAverageRepTime = mapOf(DbExercise.DEAD_LIFT to 4L,
                DbExercise.PUSH_UPS to 4L,
                DbExercise.PULL_UPS to 4L,
                DbExercise.BURPEES to 4L,
                DbExercise.SQUATS to 4L,
                DbExercise.BOX_JUMPS to 4L,
                DbExercise.KETTLE_BELL_SWINGS to 4L,
                DbExercise.DEAD_LIFT to 4L,
                DbExercise.THRUSTERS to 4L,
                DbExercise.CRUNCHES to 4L,
                DbExercise.DOUBLE_UNDERS to 4L,
                DbExercise.SINGLE_UNDERS to 4L)

    }

}


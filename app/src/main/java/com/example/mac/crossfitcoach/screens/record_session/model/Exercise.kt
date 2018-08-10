package com.example.mac.crossfitcoach.screens.record_session.model

import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class Exercise(var exerciseCode: Int,
               var startTime: Date? = null,
               var endTime: Date? = null,
               var readings: MutableList<SensorReading>? = null,
               var repDurationMs: Int = Int.MAX_VALUE,
               var name: String? = codeToNameExerciseMap[exerciseCode]) {

    init {
        if (exerciseCode == DbExercise.EXECUTION_WORKOUT || exerciseCode == DbExercise.SPEED_WORKOUT) {
            repDurationMs = Int.MAX_VALUE
        }
    }
    var state: State = State.START

    enum class State {
        START, RECORDING, STOPPED
    }

    companion object {
        val codeToNameExerciseMap = mapOf(
                DbExercise.DEAD_LIFT to "Dead Lift",
                DbExercise.PUSH_UPS to "Pushups",
                DbExercise.PULL_UPS to "Pullups",
                DbExercise.BURPEES to "Burpees",
                DbExercise.SQUATS to "Squats",
                DbExercise.BOX_JUMPS to "Box jumps",
                DbExercise.DEAD_LIFT to "KB Dead lifts",
                DbExercise.KETTLEBELL_PRESS to "KB Presses",
                DbExercise.KETTLEBELL_SQUAT_PRESS to "KB Squat Press",
                DbExercise.CRUNCHES to "Crunches",
                DbExercise.MOUNTAIN_CLIMBERS to "Mountain Climbers",
                DbExercise.WALL_BALLS to "Wall Balls",
                DbExercise.SPEED_WORKOUT to "Speed Workout",
                DbExercise.EXECUTION_WORKOUT to "Execution Workout"
        )
    }
}


package com.example.mac.crossfitcoach.screens.record_session.model

import com.example.mac.crossfitcoach.dbjava.DbExercise
import com.example.mac.crossfitcoach.dbjava.SensorReading
import java.util.*

class Exercise(var exerciseCode: Int,
               var startTime: Date? = null,
               var endTime: Date? = null,
               var readings: MutableList<SensorReading>? = null,
               var repDurationMs: Int = 3000,
               var name: String? = codeToNameExerciseMap[exerciseCode]) {

    var state: State = State.START

    enum class State {
        START, RECORDING, STOPPED
    }

    companion object {
        val codeToNameExerciseMap = mapOf(DbExercise.DEAD_LIFT to "Dead Lift",
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
                DbExercise.WALL_BALLS to "Wall Balls"
        )
    }
}


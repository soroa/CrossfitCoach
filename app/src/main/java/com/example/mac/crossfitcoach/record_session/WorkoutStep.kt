package com.example.mac.crossfitcoach.record_session

import com.example.mac.crossfitcoach.dbjava.Exercise

data class WorkoutStep(var exerciseCode:Int, var exerciseName:String? = codeToNameExerciseMap.get(exerciseCode))

val codeToNameExerciseMap = mapOf(Exercise.DEAD_LIFT to "Dead Lift",
        Exercise.PUSH_UPS to "Push ups",
        Exercise.PULL_UPS to "Pull ups",
        Exercise.BURPEES to "Burpees",
        Exercise.SQUATS to "Squats",
        Exercise.BOX_JUMPS to "Box jumps",
        Exercise.KETTLE_BELL_SWINGS to "Kettle B swings",
        Exercise.DEAD_LIFT to "Dead lifts",
        Exercise.THRUSTERS to "Thrusters")

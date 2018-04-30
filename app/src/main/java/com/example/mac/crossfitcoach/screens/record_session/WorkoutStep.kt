package com.example.mac.crossfitcoach.screens.record_session

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
        Exercise.THRUSTERS to "Thrusters",
        Exercise.CRUNCHES to "Crunches",
        Exercise.SINGLE_UNDERS to "Crunches",
        Exercise.DOUBLE_UNDERS to "Double unders"
)

val codeToRepsMap = mapOf(Exercise.DEAD_LIFT to 5,
        Exercise.PUSH_UPS to 5,
        Exercise.PULL_UPS to 5,
        Exercise.BURPEES to 5,
        Exercise.SQUATS to 5,
        Exercise.BOX_JUMPS to 5,
        Exercise.KETTLE_BELL_SWINGS to 5,
        Exercise.DEAD_LIFT to 5,
        Exercise.THRUSTERS to 5,
        Exercise.CRUNCHES to 5,
        Exercise.DOUBLE_UNDERS to 5,
        Exercise.SINGLE_UNDERS to 5)
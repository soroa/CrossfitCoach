package com.example.mac.crossfitcoach.screens.record_session.model

import android.arch.lifecycle.MutableLiveData


class Workout(exercises: List<Exercise>) {
    val liveExercises = arrayListOf<MutableLiveData<Exercise>>()

    init {
        for (ex in exercises) {
            var liveData = MutableLiveData<Exercise>()
            liveData.value = ex
            liveExercises.add(liveData)
        }
    }

    private var currentExerciseIndex = 0;

    fun getCurrentExercise(): MutableLiveData<Exercise> {
        return liveExercises.get(currentExerciseIndex)
    }

    fun nextExercise() {
        currentExerciseIndex++
    }


}
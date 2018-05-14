package com.example.mac.crossfitcoach.screens.record_session.i

import com.example.mac.crossfitcoach.screens.record_session.model.Exercise

open interface IWorkoutPresenter {
    fun getCurrentExercise():Exercise
}

interface IWorkoutWristPresenter:IWorkoutPresenter{
    fun onStartStopClicked(countdownDurationMs:Int)
    fun onSaveRecordingClicked(repCount:Int)
    fun onDiscarRecordingButtonClicked()
}
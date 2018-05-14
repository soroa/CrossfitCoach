package com.example.mac.crossfitcoach.screens.record_session.i

import com.example.mac.crossfitcoach.screens.record_session.model.Exercise

interface IWorkoutView {
    fun updateView(exercise: Exercise)
    fun connectionStatusChangeed(connected: Boolean)
    fun setWorkoutIdText(id: Long)
    fun finishWorkout()
}

interface IAnkleWorkoutView : IWorkoutView
{

    fun interruptWorkout()
}
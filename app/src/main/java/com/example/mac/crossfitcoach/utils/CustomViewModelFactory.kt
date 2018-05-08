package com.example.mac.crossfitcoach.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.example.mac.crossfitcoach.screens.record_session.RecordExerciseWristViewModel

val RECORD_EXERCISE = RecordExerciseWristViewModel::class.java.name
const val INT_VALUE = "INT_VALUE"


class CustomViewModelFactory(private val model: String, val args: Bundle, val app: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (model) {
            RECORD_EXERCISE -> return RecordExerciseWristViewModel(app) as T
            else ->
                throw Exception("bla")
        }
    }
}
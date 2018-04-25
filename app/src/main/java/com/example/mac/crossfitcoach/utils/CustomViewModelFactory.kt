package com.example.mac.crossfitcoach.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.example.mac.crossfitcoach.record_session.RecordExerciseViewModel

val RECORD_EXERCISE = RecordExerciseViewModel::class.java.name
const val INT_VALUE = "INT_VALUE"


class CustomViewModelFactory(private val model: String, val args: Bundle, val app: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (model) {
            RECORD_EXERCISE -> return RecordExerciseViewModel(app, args) as T
            else ->
                throw Exception("bla")
        }
    }
}
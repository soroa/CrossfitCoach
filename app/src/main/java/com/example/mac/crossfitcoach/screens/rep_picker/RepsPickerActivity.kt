package com.example.mac.crossfitcoach.screens.rep_picker

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_reps_input.*
import android.app.Activity
import android.content.Intent

class RepsPickerActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reps_input)
        rep_count_picker.minValue = 1
        rep_count_picker.maxValue = 100
        rep_count_picker.value = 5
        ok_btn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("rep_count", rep_count_picker.value)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
package com.example.mac.crossfitcoach.screens.exercise_list_duration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.wearable.activity.WearableActivity
import android.view.WindowManager
import android.widget.NumberPicker
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.screens.record_session.wrist.WristWorkoutPresenter
import kotlinx.android.synthetic.main.activity_duration_picker.*

class RepDurationActivity : WearableActivity() {

    companion object {
        val EXTRA_REP_DURATION = "duration"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duration_picker)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        val values = arrayOf("1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5")
        val currentVal = intent.getIntExtra(EXTRA_REP_DURATION, 1)

        ex_duration_picker.value = values.indexOf(currentVal.toString())
        ex_duration_picker.displayedValues = values
        ex_duration_picker.maxValue = values.size - 1
        ex_duration_picker.minValue = 0
        ex_duration_picker.wrapSelectorWheel = false
        ex_duration_picker.displayedValues = values
        ex_duration_picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS;
        ok_btn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_REP_DURATION, values[ex_duration_picker.value])
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

}
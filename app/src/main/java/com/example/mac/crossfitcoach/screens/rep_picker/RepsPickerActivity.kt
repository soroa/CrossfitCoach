package com.example.mac.crossfitcoach.screens.rep_picker

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_reps_input.*
import android.app.Activity
import android.content.Intent
import android.view.WindowManager

class RepsPickerActivity : FragmentActivity() {
    companion object {
        val REP_COUNT_EXTRA = "REP_COUNT_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reps_input)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        rep_count_picker.minValue = 1
        rep_count_picker.maxValue = intent.getIntExtra(REP_COUNT_EXTRA, 100)
        rep_count_picker.value = 5
        ok_btn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("rep_count", rep_count_picker.value)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
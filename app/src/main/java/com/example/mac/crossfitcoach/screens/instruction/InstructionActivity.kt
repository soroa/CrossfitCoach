package com.example.mac.crossfitcoach.screens.instruction

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_message.*

abstract class InstructionActivity : WearableActivity() {

    companion object {
        val INSTRUCTION_TEXT_EXTRA = "INSTRUCTION_TEXT_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val text = intent.extras.getString(INSTRUCTION_TEXT_EXTRA)
        instruction_text_tv.text = text
    }

}
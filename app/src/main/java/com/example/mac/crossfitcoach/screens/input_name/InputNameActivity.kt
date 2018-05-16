package com.example.mac.crossfitcoach.screens.input_name

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.example.mac.crossfitcoach.MyApplication
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.communication.ble.WorkoutCommand
import com.example.mac.crossfitcoach.screens.record_session.wrist.WorkoutWristActivity
import kotlinx.android.synthetic.main.activity_input_name.*

class InputNameActivity : FragmentActivity() {
    companion object {
        val PARTICIPANT_NAME = "participant_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_name)

        ok_btn.setOnClickListener {
            val i = Intent(this, WorkoutWristActivity::class.java)
            (application as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_START_WORKOUT, participant = "Andrea Soro"))
            i.putExtra(PARTICIPANT_NAME, "Andrea Soro")
            startActivity(i)
//            if (name.text.isNotEmpty() ||
//                    name.text.isNotBlank() ||
//                    last_name.text.isNotEmpty() ||
//                    last_name.text.isNotBlank()) {
//
//                val i = Intent(this, WorkoutWristActivity::class.java)
//                val participant = name.text.toString() + " " + last_name.text.toString()
//                (application as MyApplication).bleClient.sendMsg(WorkoutCommand(WorkoutCommand.BLE_START_WORKOUT, participant = participant))
//                i.putExtra(PARTICIPANT_NAME, participant)
//                startActivity(i)
//            } else {
//                Toast.makeText(this, "Fill out all fields", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}
package com.example.mac.crossfitcoach

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.exercise_list.ExerciseListActivity
import com.example.mac.crossfitcoach.record_session.RecordExerciseActivity
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()

        data_collect_btn.setOnClickListener { _ ->
            val i = Intent(this, ExerciseListActivity::class.java)
            startActivity(i)
        }

        sensor_log_btn.setOnClickListener { _ ->
            val db = Room.databaseBuilder(getApplication(),
                    SensorDatabase::class.java, "sensor_readings").build()
            Completable.fromAction {
                db.sensorReadingsDao().nukeTable()
            }.subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread()).subscribe()
        }
        test.setOnClickListener { _ ->
            val i = Intent(this, RecordExerciseActivity::class.java)
            startActivity(i)
        }
    }
}
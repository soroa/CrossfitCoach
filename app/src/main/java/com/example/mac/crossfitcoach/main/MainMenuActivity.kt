package com.example.mac.crossfitcoach.main

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.dbjava.SensorDatabase
import com.example.mac.crossfitcoach.record_session.RecordExerciseActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainMenuActivity : WearableActivity(), StringRecyclerAdapter.OnListItemClicked {

    private lateinit var emojis: Array<String>
    private val strings = arrayOf("Start Workout", "Delete Database")

    override fun onItemListClicked(index: Int) {
        when (index) {
            0 -> {
                val i = Intent(this, RecordExerciseActivity::class.java)
                startActivity(i)
            }
            1 -> {
                val db = Room.databaseBuilder(getApplication(),
                        SensorDatabase::class.java, "sensor_readings").build()
                Completable.fromAction {
                    db.sensorReadingsDao().nukeTable()
                }.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                {
                                    Toast.makeText(this, "Database Deleted!", Toast.LENGTH_LONG).show()
                                },
                                {
                                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                                }
                        )
            }
        }
    }

    private lateinit var stringAdapter: StringRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Enables Always-on
        emojis = arrayOf(getString(R.string.emoji_workout), getString(R.string.emoji_bomb))
        setAmbientEnabled()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        recycler_view.isEdgeItemsCenteringEnabled = true
        recycler_view.layoutManager = WearableLinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.isEdgeItemsCenteringEnabled = true;

        stringAdapter = StringRecyclerAdapter(emojis,
                strings, this)
        recycler_view.adapter = stringAdapter
    }
}
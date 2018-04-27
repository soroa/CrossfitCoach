package com.example.mac.crossfitcoach.screens.exercise_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.R
import kotlinx.android.synthetic.main.activity_exercise_list.*

class ExerciseListActivity: WearableActivity() {

    private lateinit var customAdapter: CustomRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        // Enables Always-on
        setAmbientEnabled()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        recycler_view.isEdgeItemsCenteringEnabled=true
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val exercises = arrayOf(Exercise(resources.getString(R.string.kb_swings), 0), Exercise(resources.getString(R.string.pushups), 0))

        customAdapter = CustomRecyclerAdapter(
                exercises,
                ExerciseListViewModel(this))
        recycler_view.adapter = customAdapter
    }
}
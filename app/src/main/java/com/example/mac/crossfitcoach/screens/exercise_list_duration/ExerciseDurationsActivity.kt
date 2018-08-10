package com.example.mac.crossfitcoach.screens.exercise_list_duration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.wear.widget.WearableLinearLayoutManager
import android.support.wearable.activity.WearableActivity
import com.example.mac.crossfitcoach.R
import com.example.mac.crossfitcoach.screens.record_session.BaseWorkoutPresenter
import kotlinx.android.synthetic.main.activity_main.*

class ExerciseDurationsActivity : WearableActivity(), ExerciseAdapter.OnListItemClicked {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private var lastItemClickedIndex= -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list_duration)
        initRecyclerView()
    }

    override fun onItemListClicked(index: Int) {
        lastItemClickedIndex=index
        val i = Intent(this, RepDurationActivity::class.java)
        i.putExtra(RepDurationActivity.EXTRA_REP_DURATION, BaseWorkoutPresenter.exercises[index].repDurationMs)
        startActivityForResult(i, 1)
    }

    private fun initRecyclerView() {
        recycler_view.isEdgeItemsCenteringEnabled = true
        recycler_view.layoutManager = WearableLinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.isEdgeItemsCenteringEnabled = true
        exerciseAdapter = ExerciseAdapter(BaseWorkoutPresenter.exercises.toTypedArray(), this)
        recycler_view.adapter = exerciseAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val duration = data!!.getStringExtra(RepDurationActivity.EXTRA_REP_DURATION)
                BaseWorkoutPresenter.exercises.get(lastItemClickedIndex).repDurationMs = (duration.toFloat()*1000).toInt()
                recycler_view.adapter.notifyItemChanged(lastItemClickedIndex)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
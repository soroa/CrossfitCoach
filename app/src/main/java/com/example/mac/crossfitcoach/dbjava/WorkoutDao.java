package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WorkoutDao {

    @Insert(onConflict=REPLACE)
    long save(WorkoutSession exercise);

    @Insert(onConflict=REPLACE)
    void saveAll(List<WorkoutSession> exercises);

    @Query("DELETE FROM workout_sessions")
    void nukeTable();

}
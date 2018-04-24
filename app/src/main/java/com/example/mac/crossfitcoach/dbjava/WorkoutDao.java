package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WorkoutDao {

    @Insert(onConflict=REPLACE)
    void save(WorkoutSession exercise);

    @Insert(onConflict=REPLACE)
    void saveAll(List<WorkoutSession> exercises);
}

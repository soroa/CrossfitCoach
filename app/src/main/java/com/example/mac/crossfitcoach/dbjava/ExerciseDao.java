package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExerciseDao {

    @Insert(onConflict=REPLACE)
    void save(Exercise exercise);

    @Insert(onConflict=REPLACE)
    void saveAll(List<Exercise> exercises);


}

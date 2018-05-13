package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExerciseDao {

    @Insert(onConflict = REPLACE)
    long save(DbExercise exercise);

    @Insert(onConflict = REPLACE)
    void saveAll(List<DbExercise> exercises);

    @Delete
    void delete(DbExercise... exercises);

    @Query("DELETE FROM exercises")
    void nukeTable();

}
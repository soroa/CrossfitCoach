package com.example.mac.crossfitcoach.dbjava;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import java.net.Proxy;

@TypeConverters({Converters.class})
// TODO: 24.04.18 export schema
@Database(entities = {SensorReading.class, Exercise.class, WorkoutSession.class}, version=1, exportSchema = false)
public abstract class SensorDatabase extends RoomDatabase {
    abstract public SensorReadingsDao sensorReadingsDao();
    abstract public ExerciseDao exerciseDao();
    abstract public WorkoutDao workoutDao();
}
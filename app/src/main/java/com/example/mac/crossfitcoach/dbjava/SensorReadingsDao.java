package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SensorReadingsDao {

    @Insert(onConflict = REPLACE)
    void save(SensorReading sensorReading);

    @Insert(onConflict = REPLACE)
    void saveAll(List<SensorReading> sensorReading);

    @Query("SELECT * FROM sensor_readings")
    Maybe<List<SensorReading>> load();

    @Query("DELETE FROM sensor_readings")
    void nukeTable();
}

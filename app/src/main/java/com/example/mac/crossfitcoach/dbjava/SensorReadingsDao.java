package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SensorReadingsDao {

    @Insert(onConflict=REPLACE)
    void save(SensorReading sensorReading);

    @Insert(onConflict=REPLACE)
    void saveAll(List<SensorReading> sensorReading);

    @Insert(onConflict=REPLACE)
    void load(SensorReading sensorReading);


}

package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "sensor_readings")
public class SensorReading {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name="sensor_type")
    int sensorType;
    @ColumnInfo(name="values")
    float[] values;
    @ColumnInfo(name="session_id")
    int sessionId;
    @ColumnInfo(name="exercise_id")
    int exerciseId;
    @ColumnInfo(name="timestamp")
    Date timestamp;

    public SensorReading(int sensorType, float[] values, int sessionId, int exerciseId, Date timestamp) {
        this.sensorType = sensorType;
        this.values = values;
        this.sessionId = sessionId;
        this.exerciseId = exerciseId;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getSensorType() {
        return sensorType;
    }

    public float[] getValues() {
        return values;
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

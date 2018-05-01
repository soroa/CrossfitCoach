package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "sensor_readings", foreignKeys = @ForeignKey(entity = Exercise.class,
        parentColumns = "id",
        childColumns = "exercise_id", onDelete = CASCADE))
public class SensorReading {
    public static final int ANKLE=0;
    public static final int WRIST=1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    long id;
    @ColumnInfo(name = "sensor_type")
    int sensorType;
    @ColumnInfo(name = "values")
    float[] values;
    @ColumnInfo(name = "exercise_id")
    long exerciseId;
    @ColumnInfo(name = "timestamp")
    Date timestamp;
    @ColumnInfo(name = "placement")
    int placement;

    public SensorReading(int sensorType, float[] values, long exerciseId, Date timestamp, int sensorPlacement) {
        this.sensorType = sensorType;
        this.values = values;
        this.exerciseId = exerciseId;
        this.timestamp = timestamp;
    }

    public int getSensorType() {
        return sensorType;
    }

    public float[] getValues() {
        return values;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public String toString() {
        String a = "";
        for (float f : values) {
            a = a.concat(String.valueOf(f) + " ");

        }
        return a;
    }


}

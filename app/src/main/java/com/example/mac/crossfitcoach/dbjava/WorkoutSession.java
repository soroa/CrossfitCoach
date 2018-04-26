package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "workout_sessions")
public class WorkoutSession {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name="start_time")
    Date startTime;
    @ColumnInfo(name="end_time")
    String endTime;

    public WorkoutSession(Date startTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
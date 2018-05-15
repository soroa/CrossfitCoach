package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "workout_sessions")
public class WorkoutSession {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "participant")
    String participant;
    @ColumnInfo(name = "start_time")
    Date startTime;
    @ColumnInfo(name = "end_time")
    String endTime;
    @ColumnInfo(name = "completed")
    boolean completed;


    public WorkoutSession(Date startTime, String participant) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.participant = participant;
    }
}
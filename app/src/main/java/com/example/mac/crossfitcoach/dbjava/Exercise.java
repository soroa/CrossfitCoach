package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey
    int id;
    @ColumnInfo(name="start_time")
    String startTime;
    @ColumnInfo(name="end_time")
    String endTime;
    @ColumnInfo(name="exercise_code")
    int exerciseCode;
}

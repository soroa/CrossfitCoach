package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.Date;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Entity
public class Exercise {
    public static final int PUSH_UPS = 1;
    public static final int KETTLE_BELL_SWINGS = 2;
    public static final int PULL_UPS = 3;
    public static final int BOX_JUMPS = 4;
    public static final int BURPEES = 5;
    public static final int SQUATS = 6;
    public static final int DEAD_LIFT = 7;
    public static final int THURSTERS = 8;

    @Retention(SOURCE)
    @IntDef({PULL_UPS, PUSH_UPS, KETTLE_BELL_SWINGS, BOX_JUMPS, BURPEES, SQUATS, DEAD_LIFT, THURSTERS})
    public @interface ExerciseCode {
    }

    public Exercise(Date startTime, Date endTime, int exerciseCode) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.exerciseCode = exerciseCode;
    }

    @PrimaryKey
    int id;
    @ColumnInfo(name = "start_time")
    Date startTime;
    @ColumnInfo(name = "end_time")
    Date endTime;
    @ColumnInfo(name = "exercise_code")
    @ExerciseCode
    int exerciseCode;
}

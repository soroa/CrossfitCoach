package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Entity(tableName = "exercises", foreignKeys = @ForeignKey(entity = WorkoutSession.class,
        parentColumns = "id",
        childColumns = "workout_session_id", onDelete = CASCADE))

public class DbExercise {
    public static final int PUSH_UPS = 1;
    public static final int KETTLE_BELL_SWINGS = 2;
    public static final int PULL_UPS = 3;
    public static final int BOX_JUMPS = 4;
    public static final int BURPEES = 5;
    public static final int SQUATS = 6;
    public static final int DEAD_LIFT = 7;
    public static final int KETTLE_BELL_THRUSTERS = 8;
    public static final int CRUNCHES = 11;
    public static final int MOUTAIN_CLIMBERS = 12;
    public static final int WALL_BALLS = 12;


    @Retention(SOURCE)
    @IntDef({PULL_UPS, PUSH_UPS, KETTLE_BELL_SWINGS, BOX_JUMPS, BURPEES, SQUATS, DEAD_LIFT, KETTLE_BELL_THRUSTERS})
    public @interface ExerciseCode {
    }

    public DbExercise(Date startTime, Date endTime, int exerciseCode, long sessionId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.exerciseCode = exerciseCode;
        this.sessionId = sessionId;
    }

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "start_time")
    Date startTime;
    @ColumnInfo(name = "end_time")
    Date endTime;
    @ColumnInfo(name = "workout_session_id")
    long sessionId;
    @ColumnInfo(name = "exercise_code")
    @ExerciseCode
    int exerciseCode;


}

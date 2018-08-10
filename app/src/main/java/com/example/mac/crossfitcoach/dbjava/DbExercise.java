package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntDef;

import com.example.mac.crossfitcoach.screens.record_session.model.Exercise;

import java.lang.annotation.Retention;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Entity(tableName = "exercises", foreignKeys = @ForeignKey(entity = WorkoutSession.class,
        parentColumns = "id",
        childColumns = "workout_session_id", onDelete = CASCADE))

public class DbExercise {
    public static final int PUSH_UPS = 1;
    public static final int PULL_UPS = 3;
    public static final int BOX_JUMPS = 4;
    public static final int BURPEES = 5;
    public static final int SQUATS = 6;
    public static final int DEAD_LIFT = 7;
    public static final int KETTLEBELL_SQUAT_PRESS = 8;
    public static final int KETTLEBELL_PRESS = 9;
    public static final int CRUNCHES = 11;
    public static final int WALL_BALLS = 12;
    public static final int MOUNTAIN_CLIMBERS = 13;
    public static final int SPEED_WORKOUT = 14;
    public static final int EXECUTION_WORKOUT = 15;


    @Retention(SOURCE)
    @IntDef({PULL_UPS, PUSH_UPS, BOX_JUMPS, BURPEES, SPEED_WORKOUT, EXECUTION_WORKOUT, SQUATS, DEAD_LIFT, KETTLEBELL_SQUAT_PRESS, MOUNTAIN_CLIMBERS, KETTLEBELL_PRESS, WALL_BALLS})
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
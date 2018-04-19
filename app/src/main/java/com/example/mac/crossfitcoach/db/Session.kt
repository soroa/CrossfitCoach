package com.example.mac.crossfitcoach.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Session(
        @PrimaryKey
        private val id: Int,
        @ColumnInfo(name = "start_time")
        private val startTime: String,
        @ColumnInfo(name = "end_time")
        private val endTime: String,
        @ColumnInfo(name = "movement")
        private val movement: Int)
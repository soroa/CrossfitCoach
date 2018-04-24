package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public String fromFloatArray(float[] values) {
        String converted = "";
        for (float f : values) {
            converted.concat(String.valueOf(f) + ",");
        }
        return converted;
    }

    @TypeConverter
    public float[] stringToFloat(String floatAsString) {
        if (floatAsString == null) {
            return new float[0];
        } else {
            String[] floatsStringArray = floatAsString.split(",");
            float[] floats = new float[floatsStringArray.length];
            for (int i = 0; i < floatsStringArray.length; i++) {
                floats[i] = Float.parseFloat(floatsStringArray[i]);
            }
            return floats;
        }
    }


}

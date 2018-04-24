package com.example.mac.crossfitcoach.dbjava;

import android.arch.persistence.room.TypeConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public String fromFloatArray(List<Float> values) {
        String converted = "";
        for (float f : values) {
            converted = converted.concat(String.valueOf(f) + ",");
        }
        return converted;
    }

    @TypeConverter
    public List<Float> stringToFloat(String floatAsString) {
        if (floatAsString == null) {
            return new ArrayList<>();
        } else {
            String[] floatsStringArray = floatAsString.split(",");
            ArrayList<Float> floats = new ArrayList<>();
            for (String aFloatsStringArray : floatsStringArray) {
                if(aFloatsStringArray.isEmpty()) continue;
                floats.add(Float.parseFloat(aFloatsStringArray));
            }
            return floats;
        }
    }
}

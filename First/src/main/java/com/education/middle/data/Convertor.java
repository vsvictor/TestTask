package com.education.middle.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Convertor {
    @TypeConverter
    public int fromRowStatus(RowStatus status){
        switch (status){
            case LOADED: return 1;
            case ERROR: return 2;
            case UNDEFINED: return 3;
            default: return 2;
        }
    }
    @TypeConverter
    public RowStatus toRowStatus(int i){
        if(i == 1) return RowStatus.LOADED;
        else if(i == 2) return RowStatus.ERROR;
        else  return RowStatus.UNDEFINED;
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}

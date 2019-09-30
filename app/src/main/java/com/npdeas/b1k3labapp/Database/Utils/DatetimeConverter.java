package com.npdeas.b1k3labapp.Database.Utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DatetimeConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {

        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
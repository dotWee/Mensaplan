package de.dotwee.rgb.canteen.model.database.converters;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverters {

    @TypeConverter
    public static Date dateFromLong(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {

        return date == null ? null : date.getTime();
    }
}

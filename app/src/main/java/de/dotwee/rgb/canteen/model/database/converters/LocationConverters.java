package de.dotwee.rgb.canteen.model.database.converters;

import androidx.room.TypeConverter;
import de.dotwee.rgb.canteen.model.constant.Location;

public class LocationConverters {

    @TypeConverter
    public static Location locationFromString(String value) {
        return Location.valueOf(value);
    }

    @TypeConverter
    public static String locationToString(Location location) {
        return String.valueOf(location);
    }
}

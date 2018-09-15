package de.dotwee.rgb.canteen.model.database.converters;

import androidx.room.TypeConverter;
import de.dotwee.rgb.canteen.model.constant.Type;

public class TypeConverters {

    @TypeConverter
    public static Type typeFromString(String value) {
        return Type.valueOf(value);
    }

    @TypeConverter
    public static String typeToString(Type type) {
        return String.valueOf(type);
    }

}

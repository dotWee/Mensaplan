package de.dotwee.rgb.canteen.model.database.converters;

import java.util.Arrays;

import androidx.room.TypeConverter;
import de.dotwee.rgb.canteen.model.constant.Label;

public class LabelConverters {

    @TypeConverter
    public static String labelsToString(Label[] labels) {
        return Arrays.toString(labels);
    }

    @TypeConverter
    public static Label[] labelsFromString(String value) {
        value = value.replaceAll("[\\[\\](){}]", "");

        String[] labelArray = value.split(",");
        Label[] labels = new Label[labelArray.length];
        int len = labelArray.length;

        for (int i = 0; i < len; i++) {
            for (Label label : Label.values()) {
                if (label.toString().equalsIgnoreCase(labelArray[i])) {
                    labels[i] = label;
                }
            }
        }
        return labels;
    }
}

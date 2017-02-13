package de.dotwee.rgb.canteen.model.helper;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by lukas on 19.11.2016.
 */
public class SpinnerHelper {
    private static final String TAG = SpinnerHelper.class.getSimpleName();

    public static ArrayAdapter<String> load(List<String> values, Context applicationContext) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(
                applicationContext.getApplicationContext(), android.R.layout.simple_spinner_item, values
        );

        stringArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        return stringArrayAdapter;
    }
}

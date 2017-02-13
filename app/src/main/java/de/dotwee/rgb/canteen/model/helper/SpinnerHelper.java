package de.dotwee.rgb.canteen.model.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    public static int getIndex(@NonNull Spinner spinner, @NonNull String itemValue) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(itemValue)) {
                index = i;
                break;
            }
        }

        return index;
    }
}

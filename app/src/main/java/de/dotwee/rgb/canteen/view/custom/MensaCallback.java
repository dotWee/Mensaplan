package de.dotwee.rgb.canteen.view.custom;

import android.support.annotation.NonNull;
import de.dotwee.rgb.canteen.model.Location;

import java.util.Calendar;

public interface MensaCallback {

    void onLocationSelected(@NonNull Location location);

    void onDateSelected(@NonNull Calendar calendar);

    void onDataChanged(@NonNull Location location, @NonNull Calendar calendar);
}

package de.dotwee.rgb.canteen.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.01.17.
 */
public interface Openinghours {
    static final String TAG = Openinghours.class.getSimpleName();

    @Nullable
    String getDuringLectures(@NonNull Weekday weekday);

    @Nullable
    String getOutsideLecture(@NonNull Weekday weekday);
}

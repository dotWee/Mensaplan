package de.dotwee.rgb.canteen.model.api.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.InputStream;

import de.dotwee.rgb.canteen.model.constant.Location;

/**
 * Created by lukas on 13.01.17.
 */
public interface MealProvider {

    @Nullable
    InputStream getInputStream(@NonNull Location location, int weeknumber);

}

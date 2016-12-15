package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Location {
    OTH(R.string.location_oth, "HS-R-tag"),
    OTH_EVENING(R.string.location_oth_evening, "HS-R-abend"),

    PRUEFENING(R.string.location_pruefening, "Cafeteria-Pruefening"),
    UNIVERSITY(R.string.location_university, "UNI-R");

    @StringRes
    private final int name;
    private final String nameTag;

    Location(@StringRes int name, @NonNull String nameTag) {
        this.name = name;
        this.nameTag = nameTag;
    }

    @StringRes
    public int getName() {
        return this.name;
    }

    @NonNull
    public String getNameTag() {
        return this.nameTag;
    }
}
package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.StringRes;

import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Location {
    OTH(R.string.location_oth, R.string.location_oth_tag),
    OTH_EVENING(R.string.location_oth_evening, R.string.location_oth_evening_tag),

    PRUEFENING(R.string.location_pruefening, R.string.location_pruefening_tag),
    UNIVERSITY(R.string.location_university, R.string.location_university_tag);

    @StringRes
    private final int name, nameTag;

    Location(@StringRes int name, @StringRes int nameTag) {
        this.name = name;
        this.nameTag = nameTag;
    }

    @StringRes
    public int getName() {
        return this.name;
    }

    @StringRes
    public int getNameTag() {
        return this.nameTag;
    }
}
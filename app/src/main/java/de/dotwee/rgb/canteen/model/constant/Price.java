package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.StringRes;

import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Price {
    STUDENT(R.string.price_student),
    EMPLOYEE(R.string.price_employee),
    GUEST(R.string.price_guest),
    ALL(R.string.price_all);

    private final int resId;

    Price(@StringRes int resId) {
        this.resId = resId;
    }

    @StringRes
    public int getResId() {
        return this.resId;
    }
}
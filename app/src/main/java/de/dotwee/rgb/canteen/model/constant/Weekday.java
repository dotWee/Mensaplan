package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.ColorRes;

import java.util.Calendar;

import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 19.11.2016.
 */
public enum Weekday {
    MONDAY(Calendar.MONDAY, R.color.md_red_600, R.color.md_red_800),
    TUESDAY(Calendar.TUESDAY, R.color.md_purple_600, R.color.md_purple_800),
    WEDNESDAY(Calendar.WEDNESDAY, R.color.md_deep_purple_600, R.color.md_deep_purple_800),
    THURSDAY(Calendar.THURSDAY, R.color.md_indigo_600, R.color.md_indigo_800),
    FRIDAY(Calendar.FRIDAY, R.color.md_blue_600, R.color.md_blue_800),
    SATURDAY(Calendar.SATURDAY, R.color.md_teal_600, R.color.md_teal_800),
    SUNDAY(Calendar.SUNDAY, R.color.md_brown_600, R.color.md_brown_800);

    private final int dayOfWeek;

    @ColorRes
    private final int colorId;

    @ColorRes
    private final int darkColorId;

    Weekday(int dayOfWeek, @ColorRes int colorId, @ColorRes int darkColorId) {
        this.dayOfWeek = dayOfWeek;
        this.colorId = colorId;
        this.darkColorId = darkColorId;
    }

    public int getDayOfWeek() {
        return this.dayOfWeek;
    }

    @ColorRes
    public int getColorId() {
        return colorId;
    }

    @ColorRes
    public int getDarkColorId() {
        return darkColorId;
    }
}

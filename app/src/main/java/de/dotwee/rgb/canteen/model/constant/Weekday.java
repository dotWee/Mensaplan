package de.dotwee.rgb.canteen.model.constant;

import java.util.Calendar;

/**
 * Created by lukas on 19.11.2016.
 */
public enum Weekday {
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY);

    private final int dayOfWeek;

    Weekday(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return this.dayOfWeek;
    }
}

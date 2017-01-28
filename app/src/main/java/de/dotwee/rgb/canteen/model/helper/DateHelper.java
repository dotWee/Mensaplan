package de.dotwee.rgb.canteen.model.helper;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.11.2016.
 */
public class DateHelper {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault());
    private static final String TAG = DateHelper.class.getSimpleName();

    public static int getCurrentWeeknumber() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    @NonNull
    public static String format(@NonNull Date date) {
        return format(date, DATE_FORMAT);
    }

    @NonNull
    public static String format(@NonNull Date date, @NonNull SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(date);
    }

    @NonNull
    public static String format(@NonNull Calendar calendar, @NonNull SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(calendar.getTime());
    }

    @NonNull
    public static String format(@NonNull Weekday weekday, @NonNull SimpleDateFormat simpleDateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekday.getDayOfWeek());

        return format(calendar, simpleDateFormat);
    }

    @NonNull
    public static Weekday getWeekday(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (Weekday weekday : Weekday.values()) {
            if (weekday.getDayOfWeek() == dayOfWeek) {
                return weekday;
            }
        }

        throw new IllegalStateException("Couldn't find weekday for weekday=" + calendar.get(Calendar.DAY_OF_WEEK));
    }
}

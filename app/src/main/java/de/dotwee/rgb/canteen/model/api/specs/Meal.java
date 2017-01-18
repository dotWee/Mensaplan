package de.dotwee.rgb.canteen.model.api.specs;

import android.support.v4.util.ArrayMap;

import de.dotwee.rgb.canteen.model.constant.Location;
import timber.log.Timber;

/**
 * Created by lukas on 15.01.17.
 * <p>
 * Example hierarchy:
 * Meal
 * | OTH
 * |  Monday
 * |  Tuesday
 * | UNI
 * |  Monday
 */
public class Meal extends ArrayMap<Location, WeekMeal> {
    private static final String TAG = Meal.class.getSimpleName();
    private final int weeknumber;

    public Meal(int weeknumber) {
        this.weeknumber = weeknumber;
    }

    @Override
    public WeekMeal put(Location key, WeekMeal value) {
        Timber.i("New entry for location=%s: weekmeal-size=%d", key.getNameTag(), value.size());
        return super.put(key, value);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "weeknumber=" + weeknumber +
                '}';
    }
}

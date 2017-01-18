package de.dotwee.rgb.canteen.model.api.specs;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.11.2016.
 */
public class WeekMeal extends ArrayMap<Weekday, DayMeal> {
    private static final String TAG = WeekMeal.class.getSimpleName();
    private final Location location;

    public WeekMeal(@NonNull List<Item> items, @NonNull Location location) {
        this.location = location;

        // Init map
        for (Weekday weekday : Weekday.values()) {
            List<Item> itemsPerDay = getItemsByDay(items, weekday);
            DayMeal dayMeal = new DayMeal(weekday, itemsPerDay);
            put(weekday, dayMeal);
        }
    }

    private List<Item> getItemsByDay(@NonNull List<Item> items, @NonNull Weekday weekday) {
        List<Item> temp = new ArrayList<>();
        for (Item item : items) {
            if (weekday == item.getWeekday()) {
                temp.add(item);
            }
        }

        return temp;
    }

    public int fullSize() {
        int items = 0;

        for (Weekday weekday : keySet()) {
            DayMeal dayMeal = get(weekday);
            items += dayMeal.getItemsCount();
        }

        return items;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }
}

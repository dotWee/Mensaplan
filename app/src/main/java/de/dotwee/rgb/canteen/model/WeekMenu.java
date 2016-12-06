package de.dotwee.rgb.canteen.model;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.11.2016.
 */
public class WeekMenu extends ArrayMap<Weekday, DayMenu> {
    private static final String TAG = WeekMenu.class.getSimpleName();

    public WeekMenu(@NonNull List<Item> items) {

        // Init map
        for (Weekday weekday : Weekday.values()) {
            List<Item> itemsPerDay = getItemsByDay(items, weekday);
            DayMenu dayMenu = new DayMenu(weekday, itemsPerDay);
            put(weekday, dayMenu);
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
}

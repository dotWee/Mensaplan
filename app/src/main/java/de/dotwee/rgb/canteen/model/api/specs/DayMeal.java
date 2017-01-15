package de.dotwee.rgb.canteen.model.api.specs;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.11.2016.
 */
public class DayMeal extends ArrayMap<Type, ArrayList<Item>> {
    private static final String TAG = DayMeal.class.getSimpleName();
    private final Weekday weekday;

    public DayMeal(@NonNull Weekday weekday, @NonNull List<Item> itemsPerDay) {
        this.weekday = weekday;

        for (Item item : itemsPerDay) {
            Type type = item.getType();

            // If doesn't exist, create new list
            if (!containsKey(type)) {
                put(type, new ArrayList<Item>());
            }

            ArrayList<Item> existingItems = get(type);
            if (weekday == item.getWeekday()) {
                existingItems.add(item);
                put(type, existingItems);
            }
        }
    }

    public List<Type> getContainedTypes() {
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
            if (containsKey(type)) {
                types.add(type);
            }
        }

        return types;
    }

    @NonNull
    public Weekday getWeekday() {
        return weekday;
    }

    public int getItemsCount() {
        int count = 0;

        for (Type type : Type.values()) {
            if (containsKey(type)) {
                count++;

                count += getItemsCount(type);
            }
        }

        return count;
    }

    private int getItemsCount(Type type) {
        if (containsKey(type)) {
            return get(type).size();
        } else return 0;
    }
}

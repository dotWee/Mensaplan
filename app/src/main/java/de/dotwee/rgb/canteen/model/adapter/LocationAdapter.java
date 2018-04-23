package de.dotwee.rgb.canteen.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.widget.ArrayAdapter;

import java.util.Map;

import de.dotwee.rgb.canteen.model.Location;

public class LocationAdapter extends ArrayAdapter<String> {
    private Map<Location, String> locationMap;

    public LocationAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item);

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationMap = new ArrayMap<>();
        for (Location location : Location.values()) {
            String name = getContext().getString(location.getNameVal());

            locationMap.put(location, name);
            add(name);
        }

        notifyDataSetChanged();

    }

    @NonNull
    public Location getLocationByName(String name) {
        for (Map.Entry<Location, String> entry : locationMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException(String.format("Name with value %s does not exist!", name));
    }
}

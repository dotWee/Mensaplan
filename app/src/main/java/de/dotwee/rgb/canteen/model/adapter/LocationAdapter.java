package de.dotwee.rgb.canteen.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Map;

import de.dotwee.rgb.canteen.model.Location;

public class LocationAdapter extends ArrayAdapter<Location> {
    private Map<Location, String> locationMap;

    public LocationAdapter(@NonNull Context context) {
        super(context, 0);

        locationMap = new ArrayMap<>();
        for (Location location : Location.values()) {
            String name = getContext().getString(location.getNameVal());
            locationMap.put(location, name);
            add(location);
        }

        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        setLocationName(convertView, position);
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        setLocationName(convertView, position);
        return convertView;
    }

    private void setLocationName(@NonNull View itemView, int position) {
        Location location = Location.values()[position];

        TextView textView = itemView.findViewById(android.R.id.text1);
        textView.setText(locationMap.get(location));
    }
}

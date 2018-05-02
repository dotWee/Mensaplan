package de.dotwee.rgb.canteen.view.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import de.dotwee.rgb.canteen.model.Location;

public class LocationTabLayout extends TabLayout implements TabLayout.OnTabSelectedListener {
    Callback callback;

    public LocationTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (Location location : Location.values()) {
            Tab tab = newTab();

            tab.setText(location.getNameVal());
            tab.setTag(location);
            addTab(tab);
        }

        addOnTabSelectedListener(this);
    }

    public LocationTabLayout(Context context) {
        this(context, null);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onTabSelected(Tab tab) {
        if (callback != null) {
            Location location = (Location) tab.getTag();
            assert location != null;

            callback.onLocationSelected(location);
        }
    }

    @Override
    public void onTabUnselected(Tab tab) {

    }

    @Override
    public void onTabReselected(Tab tab) {
        onTabSelected(tab);
    }

    public interface Callback {

        void onLocationSelected(@NonNull Location location);
    }
}

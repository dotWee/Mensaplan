package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;

import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 12.11.2016.
 */

public interface MainPresenter {

    void onSettingsOptionClick(@NonNull MenuItem menuItem);

    void onIngredientsOptionClick(@NonNull MenuItem menuItem);

    void onLocationSelected(@NonNull Location location);

    void onDateSelected(@NonNull Weekday weekday);

    void onSwipeRefresh(@NonNull SwipeRefreshLayout swipeRefreshLayout);
}

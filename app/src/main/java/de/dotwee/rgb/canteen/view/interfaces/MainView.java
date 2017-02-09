package de.dotwee.rgb.canteen.view.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 09.02.17.
 */
public interface MainView {
    String TAG = MainView.class.getSimpleName();

    void setRefreshing(boolean refreshing);

    void setDataset(@Nullable DayMeal daymeal);

    void showSnackbar();

    void showIngredientsDialog(@Nullable Item item);


    interface SettingView {

        @NonNull
        Location getSelectedLocation();


        @NonNull
        Weekday getSelectedWeekday();
    }

    interface MenuView {

        void onSettingsOptionClick();

        void onIngredientsOptionClick();
    }
}

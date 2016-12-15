package de.dotwee.rgb.canteen.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;

import java.net.UnknownHostException;

import de.dotwee.rgb.canteen.model.DayMenu;
import de.dotwee.rgb.canteen.model.WeekMenu;
import de.dotwee.rgb.canteen.model.adapter.DayMenuAdapter;
import de.dotwee.rgb.canteen.model.api.MealRunnable;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.events.OnItemClickEvent;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.model.threads.DefaultExecutorSupplier;
import de.dotwee.rgb.canteen.view.MainActivity;
import de.dotwee.rgb.canteen.view.SettingsActivity;
import de.dotwee.rgb.canteen.view.dialogs.IngredientsDialog;
import timber.log.Timber;

/**
 * Created by lukas on 13.11.2016.
 */
public class MainPresenterImpl implements MainPresenter, MealRunnable.Receiver {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();
    private final MainActivity mainActivity;
    private Location location;
    private Weekday weekday = Weekday.MONDAY;
    private IngredientsDialog ingredientsDialog = null;
    private WeekMenu weekMenu;

    private boolean isMealRunnableRunning = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MainPresenterImpl(@NonNull final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.swipeRefreshLayout = mainActivity.swipeRefreshLayout;

        ingredientsDialog = new IngredientsDialog(mainActivity);
    }

    @Override
    public void onSettingsOptionClick(@NonNull MenuItem menuItem) {
        Intent intent = new Intent(mainActivity, SettingsActivity.class);
        mainActivity.startActivity(intent);
    }

    @Override
    public void onIngredientsOptionClick(@NonNull MenuItem menuItem) {

        // Reset ingredients dialog to show all details
        ingredientsDialog.setItemInfo(null);

        if (!ingredientsDialog.isShowing()) {
            ingredientsDialog.show();
        }
    }

    @Override
    public void onItemClickEvent(@NonNull OnItemClickEvent onItemClickEvent) {
        ingredientsDialog.setItemInfo(onItemClickEvent.getItemInfo());

        if (!ingredientsDialog.isShowing()) {
            ingredientsDialog.show();
        }
    }

    @Override
    public void onLocationSelected(@NonNull Location location) {
        Timber.i("Changed visible location to %s", location.getName());
        this.location = location;
        performDataRefresh();
    }

    @Override
    public void onDateSelected(@NonNull Weekday weekday) {
        Timber.i("Changed visible date to %s", weekday.name());
        this.weekday = weekday;
        performDataRefresh();
    }

    @Override
    public void onSwipeRefresh(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        performDataRefresh();
    }

    @Override
    public void onDataLoaded(@NonNull WeekMenu weekMenu) {
        Timber.i("onDataLoaded");

        isMealRunnableRunning = false;
        this.weekMenu = weekMenu;
        handleDataRefresh();
    }

    private void handleDataRefresh() {
        if (weekMenu == null) {
            Timber.e("WeekMenu is null!");
            return;
        }

        if (weekday == null) {
            Timber.e("Weekday is null! WeekMenuSize=%s", weekMenu.size());
            return;
        }

        final DayMenu dayMenu = weekMenu.get(weekday);
        if (dayMenu == null) {
            Timber.e("DayMenu is null! WeekMenuSize=%s WeekDay=%s", weekMenu.size(), weekday.getDayOfWeek());
            return;
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DayMenuAdapter dayMenuAdapter = (DayMenuAdapter) mainActivity.recyclerView.getAdapter();
                dayMenuAdapter.setDayMenu(dayMenu);
                dayMenuAdapter.notifyDataSetChanged();

                mainActivity.recyclerView.invalidate();
                updateSwipeRefreshState();
            }
        });
    }

    /**
     * TODO: Allow custom weeknumber
     */
    private void performDataRefresh() {
        if (!isMealRunnableRunning) {
            Timber.i("Perform data refresh");

            isMealRunnableRunning = true;
            swipeRefreshLayout.setRefreshing(true);

            String locationTag = location.getNameTag();
            MealRunnable mealRunnable = new MealRunnable(this, locationTag, DateHelper.getCurrentWeeknumber(), mainActivity.getCacheDir());
            DefaultExecutorSupplier.getInstance().executeBackgroundTask(mealRunnable);
        }
    }

    private void updateSwipeRefreshState() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isMealRunnableRunning);
            }
        });
    }

    @Override
    public void onDataError(@NonNull Exception exception) {
        Timber.i("onDataError");

        isMealRunnableRunning = false;
        Timber.e(exception);

        String snackbarString;
        if (exception instanceof UnknownHostException) {
            snackbarString = "Please make sure you're connected to a working network.";
        } else {
            snackbarString = "There appeared an unknown error while refreshing";
        }

        updateSwipeRefreshState();
        mainActivity.showSnackbar(snackbarString, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performDataRefresh();
            }
        });
    }
}

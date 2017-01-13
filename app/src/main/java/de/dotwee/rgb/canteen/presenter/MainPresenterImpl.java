package de.dotwee.rgb.canteen.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;

import java.net.UnknownHostException;

import de.dotwee.rgb.canteen.model.adapter.DayMealAdapter;
import de.dotwee.rgb.canteen.model.api.provider.MealProvider;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.events.OnItemClickEvent;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.activities.MainActivity;
import de.dotwee.rgb.canteen.view.activities.SettingsActivity;
import de.dotwee.rgb.canteen.view.activities.SplashActivity;
import de.dotwee.rgb.canteen.view.dialogs.IngredientsDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lukas on 13.11.2016.
 */
public class MainPresenterImpl implements MainPresenter, Observer<WeekMeal> {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();
    private final MainActivity mainActivity;
    private Location location;
    private Weekday weekday = Weekday.MONDAY;
    private IngredientsDialog ingredientsDialog = null;

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
        ingredientsDialog.setItem(null);

        if (!ingredientsDialog.isShowing()) {
            ingredientsDialog.show();
        }
    }

    @Override
    public void onItemClickEvent(@NonNull OnItemClickEvent onItemClickEvent) {
        ingredientsDialog.setItem(onItemClickEvent.getItem());

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

    /**
     * TODO: Allow custom weeknumber
     */
    private void performDataRefresh() {
        if (!isMealRunnableRunning) {
            Timber.i("Perform data refresh");

            isMealRunnableRunning = true;
            swipeRefreshLayout.setRefreshing(true);

            String locationTag = location.getNameTag();

            MealProvider.getObservable(locationTag, DateHelper.getCurrentWeeknumber(), mainActivity.getCacheDir())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
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
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(WeekMeal weekMeal) {
        Timber.i("onDataLoaded");

        if (weekday == null) {
            Timber.e("Weekday is null! WeekMenuSize=%s", weekMeal.size());
            return;
        }

        final DayMeal dayMeal = weekMeal.get(weekday);
        if (dayMeal == null) {
            Timber.e("DayMeal is null! WeekMenuSize=%s WeekDay=%s", weekMeal.size(), weekday.getDayOfWeek());
            return;
        }

        DayMealAdapter dayMealAdapter = (DayMealAdapter) mainActivity.recyclerView.getAdapter();
        dayMealAdapter.setDayMeal(dayMeal);
        dayMealAdapter.notifyDataSetChanged();

        mainActivity.recyclerView.invalidate();
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);

        String snackbarString;
        if (e instanceof UnknownHostException) {
            snackbarString = "Please make sure you're connected to a working network.";
            mainActivity.showSnackbar(snackbarString, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performDataRefresh();
                }
            });
        } else {

            //snackbarString = "There appeared an unknown error while refreshing";
            Intent intent = new Intent(mainActivity.getApplicationContext(), SplashActivity.class);
            intent.putExtra(SplashActivity.class.getSimpleName(), SplashActivity.INTENT_FORCE_REFRESH);
            mainActivity.startActivity(intent);
        }
    }

    @Override
    public void onComplete() {
        isMealRunnableRunning = false;
        updateSwipeRefreshState();

    }
}

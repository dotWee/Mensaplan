package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.view.adapter.DayMealAdapter;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.events.RxOnItemBus;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.model.helper.PreferencesHelper;
import de.dotwee.rgb.canteen.model.helper.SpinnerHelper;
import de.dotwee.rgb.canteen.presenter.MainPresenter;
import de.dotwee.rgb.canteen.presenter.MainPresenterImpl;
import de.dotwee.rgb.canteen.view.dialogs.IngredientsDialog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainView, MainView.MenuView, MainView.SettingView, Spinner.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, Observer<Item> {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.activity_main)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinnerLocation)
    Spinner spinnerLocation;

    @BindView(R.id.spinnerDate)
    Spinner spinnerDate;

    @BindView(R.id.textViewEmpty)
    TextView textViewEmpty;

    private IngredientsDialog ingredientsDialog;
    private MainPresenter mainPresenter;
    private DayMealAdapter dayMealAdapter;

    @Override
    protected void onStart() {
        super.onStart();

        RxOnItemBus.subscribeTo(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag(TAG);

        // Init view
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ingredientsDialog = new IngredientsDialog(this);

        setupRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(this);
        setupLocationSpinner();
        setupDateSpinner();

        if (mainPresenter == null) {
            mainPresenter = new MainPresenterImpl(this, this, getCacheDir());
        }

        setSupportActionBar(toolbar);

        // Set location to last selection
        Location lastLocation = PreferencesHelper.getLastLocation();
        String lastLocationValue = getString(lastLocation.getName());
        spinnerLocation.setSelection(SpinnerHelper.getIndex(spinnerLocation, lastLocationValue));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Redraw RecyclerView
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item == null) {
            return false;
        }

        switch (item.getItemId()) {

            case R.id.actionIngredients:
                onIngredientsOptionClick();
                return true;

            case R.id.actionSettings:
                onSettingsOptionClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupLocationSpinner() {
        List<String> values = new ArrayList<>();
        for (Location location : Location.values()) {
            values.add(getString(location.getName()));
        }

        ArrayAdapter<String> locationAdapter = SpinnerHelper.load(values, this);
        setupSpinner(spinnerLocation, locationAdapter);
    }

    private void setupDateSpinner() {
        List<String> values = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Weekday weekday : Weekday.values()) {
            calendar.set(Calendar.DAY_OF_WEEK, weekday.getDayOfWeek());

            String value = DateHelper.format(calendar.getTime());
            values.add(value);
        }

        ArrayAdapter<String> dateAdapter = SpinnerHelper.load(values, this);
        setupSpinner(spinnerDate, dateAdapter);
    }

    private void setupSpinner(Spinner spinner, ArrayAdapter arrayAdapter) {
        spinner.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        dayMealAdapter = new DayMealAdapter();
        recyclerView.setAdapter(dayMealAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void setDataset(@Nullable DayMeal daymeal) {
        if (daymeal != null) {
            dayMealAdapter.setDayMeal(daymeal);
            dayMealAdapter.notifyDataSetChanged();
        }

        recyclerView.invalidate();
        setRefreshing(false);
    }

    @Override
    public void showSnackbar() {
        String snackbarString = "There appeared an unknown error while refreshing.";
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackbarString, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dialog_action_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.putExtra(SplashActivity.class.getSimpleName(), SplashActivity.INTENT_FORCE_REFRESH);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showIngredientsDialog(@Nullable Item item) {

        // If null: Reset ingredients dialog to show all details
        ingredientsDialog.setItem(item);

        if (!ingredientsDialog.isShowing()) {
            ingredientsDialog.show();
        }
    }

    @Override
    public void showNoDataView(boolean isDataAvailable) {
        Timber.i("showNoDataView=%b", isDataAvailable);

        recyclerView.setVisibility(isDataAvailable ? View.VISIBLE : View.GONE);
        textViewEmpty.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spinnerLocation) {
            mainPresenter.onLocationChange();

        } else if (adapterView == spinnerDate) {
            mainPresenter.onWeekdayChange();

        }
    }

    /*
    @SuppressWarnings("unused") // EventBus method don't get detected by lint
    @Subscribe
    public void onItemClickEvent(@NonNull OnItemClickEvent onItemClickEvent) {
        Item item = onItemClickEvent.getItem();
        showIngredientsDialog(item);
    }
    */


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRefresh() {
        // Force a refresh by acting like the user changed the location
        mainPresenter.onLocationChange();
    }


    @Override
    public void onSettingsOptionClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onIngredientsOptionClick() {
        showIngredientsDialog(null);
    }

    @NonNull
    @Override
    public Location getSelectedLocation() {

        String value = spinnerLocation.getSelectedItem().toString();
        for (Location location : Location.values()) {
            if (getString(location.getName()).equalsIgnoreCase(value)) {

                return location;
            }
        }

        Timber.e("Couldn't detect location from spinner. Value from adapter=%s", value);
        return Location.OTH;
    }

    @NonNull
    @Override
    public Weekday getSelectedWeekday() {

        String value = spinnerDate.getSelectedItem().toString();
        try {
            Date date = DateHelper.DATE_FORMAT.parse(value);
            return DateHelper.getWeekday(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        Timber.e("Couldn't detect weekday from spinner. Value from adapter=%s", value);
        return Weekday.MONDAY;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Item item) {
        showIngredientsDialog(item);
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);
    }

    @Override
    public void onComplete() {

    }
}

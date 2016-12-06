package de.dotwee.rgb.canteen.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.adapter.DayMenuAdapter;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.model.helper.SpinnerHelper;
import de.dotwee.rgb.canteen.presenter.MainPresenter;
import de.dotwee.rgb.canteen.presenter.MainPresenterImpl;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activity_main)
    CoordinatorLayout coordinatorLayout;
    DayMenuAdapter dayMenuAdapter;

    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinnerLocation)
    Spinner spinnerLocation;
    @BindView(R.id.spinnerDate)
    Spinner spinnerDate;
    private MainPresenter mainPresenter;
    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> dateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag(TAG);

        // Init view
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenterImpl(this);
        setupRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(this);
        setupLocationSpinner();
        setupDateSpinner();

        setSupportActionBar(toolbar);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionSettings:
                mainPresenter.onSettingsOptionClick(item);
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

        locationAdapter = SpinnerHelper.load(values, this);
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

        dateAdapter = SpinnerHelper.load(values, this);
        setupSpinner(spinnerDate, dateAdapter);
    }

    private void setupSpinner(Spinner spinner, ArrayAdapter arrayAdapter) {
        spinner.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()), PorterDuff.Mode.SRC_ATOP);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        dayMenuAdapter = new DayMenuAdapter();
        recyclerView.setAdapter(dayMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void showSnackbar(View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "There appeared an error while refreshing", Snackbar.LENGTH_SHORT);
        if (actionListener != null) {
            snackbar.setAction("Retry", actionListener);
        }

        snackbar.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == spinnerLocation) {
            String value = spinnerLocation.getSelectedItem().toString();
            for (Location location : Location.values()) {
                if (getString(location.getName()).equalsIgnoreCase(value)) {

                    mainPresenter.onLocationSelected(location);
                }
            }
        } else if (adapterView == spinnerDate) {
            String value = spinnerDate.getSelectedItem().toString();
            try {
                Date date = DateHelper.DATE_FORMAT.parse(value);
                Weekday weekday = DateHelper.getWeekday(date);
                mainPresenter.onDateSelected(weekday);
            } catch (ParseException e) {
                e.printStackTrace();
                Timber.e(e);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onRefresh() {
        mainPresenter.onSwipeRefresh(swipeRefreshLayout);
    }
}

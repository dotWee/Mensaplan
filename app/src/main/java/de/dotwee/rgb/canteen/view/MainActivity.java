package de.dotwee.rgb.canteen.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.Location;
import de.dotwee.rgb.canteen.model.adapter.ItemRecyclerViewAdapter;
import de.dotwee.rgb.canteen.model.proxy.MensaProxy;
import de.dotwee.rgb.canteen.model.proxy.MensaProxyImpl;
import de.dotwee.rgb.canteen.model.proxy.RequestParser;
import de.dotwee.rgb.canteen.view.custom.LocationTabLayout;
import de.dotwee.rgb.canteen.view.custom.MensaCallback;
import okhttp3.Call;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, MensaCallback {

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutLocation)
    LocationTabLayout locationTabLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ItemRecyclerViewAdapter itemRecyclerViewAdapter = new ItemRecyclerViewAdapter();
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM d", Locale.getDefault());

    // TODO enable setting own defaults
    Calendar calendar = Calendar.getInstance();
    Location locationSelected = Location.OTH;
    MensaProxy mensaProxy = new MensaProxyImpl(null);
    int selectedWeekday = Calendar.MONDAY;
    MenuItem menuItemDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        locationTabLayout.setCallback(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        onDateSelected(calendar);
    }

    @Override
    public void onLocationSelected(@NonNull Location location) {
        locationSelected = location;
        // TODO update view with location

        Toast.makeText(this, "Location " + locationSelected.getNameTag() + " has been selected", Toast.LENGTH_SHORT).show();
        onDataChanged(locationSelected, calendar);
    }

    @Override
    public void onDateSelected(@NonNull Calendar calendar) {

        // TODO update view with changed view
        selectedWeekday = calendar.get(Calendar.WEEK_OF_YEAR);
        Toast.makeText(this, "Weeknumber " + calendar.get(Calendar.WEEK_OF_YEAR) + " has been selected", Toast.LENGTH_SHORT).show();

        // Apply date to menu item
        String dateValue = simpleDateFormat.format(calendar.getTime());
        menuItemDate.setTitle(dateValue);

        onDataChanged(locationSelected, calendar);
    }

    @Override
    public void onDataChanged(@NonNull Location location, @NonNull Calendar calendar) {

        HttpUrl httpUrl = mensaProxy.getHttpUrl(location, calendar.get(Calendar.WEEK_OF_YEAR));
        RequestParser requestParser = new RequestParser(new de.dotwee.rgb.canteen.model.proxy.MensaCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull ArrayList<Item> items) {
                itemRecyclerViewAdapter.setItems(items);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        mensaProxy.newCall(requestParser, httpUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItemDate = menu.findItem(R.id.menuItemDate);

        // Apply date on startup
        onDateSelected(calendar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItemDate) {
            Toast.makeText(this, "Clicked on date menu item", Toast.LENGTH_SHORT).show();

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setThemeDark(true);
            dpd.show(getFragmentManager(), "Datepickerdialog");

            return true;
        } else {
            return false;
        }
    }
}

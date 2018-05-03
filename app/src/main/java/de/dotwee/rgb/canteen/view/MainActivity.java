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
import de.dotwee.rgb.canteen.model.Location;
import de.dotwee.rgb.canteen.model.adapter.ItemRecyclerViewAdapter;
import de.dotwee.rgb.canteen.view.custom.LocationTabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LocationTabLayout.Callback {

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutLocation)
    LocationTabLayout locationTabLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ItemRecyclerViewAdapter itemRecyclerViewAdapter = new ItemRecyclerViewAdapter();

    // TODO enable setting own defaults
    Location locationSelected = Location.OTH;
    int selectedWeekday = Calendar.MONDAY;

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // TODO update view with changed view
        selectedWeekday = calendar.get(Calendar.WEEK_OF_YEAR);
        Toast.makeText(this, "Weeknumber " + calendar.get(Calendar.WEEK_OF_YEAR) + " has been selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationSelected(@NonNull Location location) {
        locationSelected = location;
        // TODO update view with location

        Toast.makeText(this, "Location " + locationSelected.getNameTag() + " has been selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

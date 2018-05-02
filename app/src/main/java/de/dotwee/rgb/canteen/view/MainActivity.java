package de.dotwee.rgb.canteen.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.Location;
import de.dotwee.rgb.canteen.view.custom.LocationTabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LocationTabLayout.Callback {

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutLocation)
    LocationTabLayout locationTabLayout;

    // TODO enable setting own default location
    Location locationSelected = Location.OTH;

    @NonNull
    public Location getSelectedLocation() {
        return locationSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        locationTabLayout.setCallback(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // TODO update view with changed view
        Toast.makeText(this, "Weeknumber " + calendar.get(Calendar.WEEK_OF_YEAR) + " has been selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationSelected(@NonNull Location location) {
        locationSelected = location;
        // TODO update view with location

        Toast.makeText(this, "Location " + locationSelected.getNameTag() + " has been selected", Toast.LENGTH_SHORT).show();
    }
}

package de.dotwee.rgb.canteen.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.Location;
import de.dotwee.rgb.canteen.model.adapter.LocationAdapter;

public class MainActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinnerLocation)
    Spinner spinnerLocation;
    LocationAdapter adapterLocation;
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

        adapterLocation = new LocationAdapter(this);

        spinnerLocation.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()), PorterDuff.Mode.SRC_ATOP);
        spinnerLocation.setAdapter(adapterLocation);
        spinnerLocation.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerLocation) {
            String locationName = spinnerLocation.getSelectedItem().toString();

            // TODO update view with location
            locationSelected = adapterLocation.getLocationByName(locationName);

            Toast.makeText(this, "Locaion " + locationSelected.getNameTag() + " has been selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

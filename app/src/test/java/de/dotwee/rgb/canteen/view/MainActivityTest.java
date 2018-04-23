package de.dotwee.rgb.canteen.view;

import android.widget.Spinner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.adapter.LocationAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void selectLocation_shouldChangeSelectedLocation() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        int selectionPosition = 2;

        Spinner locationSpinner = mainActivity.findViewById(R.id.spinnerLocation);
        LocationAdapter locationAdapter = (LocationAdapter) locationSpinner.getAdapter();

        String expectedSelectedLocationName = locationAdapter.getItem(selectionPosition - 2);
        String actualSelectedLocationName = (String) locationSpinner.getSelectedItem();

        locationSpinner.setSelection(selectionPosition, true);
        assertEquals(expectedSelectedLocationName, actualSelectedLocationName);
    }
}
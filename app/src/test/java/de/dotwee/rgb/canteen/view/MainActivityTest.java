package de.dotwee.rgb.canteen.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void selectLocation_shouldChangeSelectedLocation() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        /*
        for (Location expectedLocation : Location.values()) {
            Spinner locationSpinner = mainActivity.findViewById(R.id.spinnerLocation);
            LocationAdapter locationAdapter = (LocationAdapter) locationSpinner.getAdapter();
            int selectionPosition = locationAdapter.getPosition(expectedLocation);

            locationSpinner.setSelection(selectionPosition, true);
            assertEquals(expectedLocation, (Location) locationSpinner.getSelectedItem());
        }
        */
    }
}
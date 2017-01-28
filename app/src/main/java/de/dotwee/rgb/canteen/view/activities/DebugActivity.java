package de.dotwee.rgb.canteen.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.view.custom.OpeninghoursView;

/**
 * Created by lukas on 20.01.17.
 */
public class DebugActivity extends AppCompatActivity {
    private static final String TAG = DebugActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OpeninghoursView openinghoursView = new OpeninghoursView(this);
        setContentView(openinghoursView);
    }
}

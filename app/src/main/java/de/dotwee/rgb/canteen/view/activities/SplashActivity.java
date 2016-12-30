package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.api.data.CacheRunnable;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.dialogs.ClosedDialog;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements CacheRunnable.Receiver {
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static int INTENT_FORCE_REFRESH = 0;
    private ClosedDialog closedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        closedDialog = new ClosedDialog(this);
        performCheck();
    }

    public void performCheck() {
        if (getIntent().getIntExtra(TAG, -1) == INTENT_FORCE_REFRESH) {
            new CacheRunnable(DateHelper.getCurrentWeeknumber(), getCacheDir(), this);
        } else {
            this.onFinished();
        }
    }

    @Override
    public void onFinished() {
        Timber.i("onFinished CacheRunnable");

        Location[] locations = CacheHelper.getCached(getCacheDir(), DateHelper.getCurrentWeeknumber());
        Timber.i("LocationArrayLength=%d", locations.length);

        // Check if there are any cached locations for this week
        if (locations.length != -1) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            Timber.w("No data available!");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closedDialog.show();
                }
            });
        }
    }
}
package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.api.data.CacheRunnable;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements CacheRunnable.Receiver {
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static int INTENT_FORCE_REFRESH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

        if (locations.length != -1) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            Timber.w("No data available!");
        }
    }
}
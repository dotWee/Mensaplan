package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.api.data.CacheRunnable;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements CacheRunnable.Receiver {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        Timber.i("Creating new CacheRunnable");

        if (CacheHelper.exists(getCacheDir(), DateHelper.getCurrentWeeknumber())) {
            // All week menus are already cached
            this.onFinished();

        } else {
            new CacheRunnable(DateHelper.getCurrentWeeknumber(), getCacheDir(), this);
        }
    }

    @Override
    public void onFinished() {
        Timber.i("onFinished CacheRunnable");
        startActivity(new Intent(this, MainActivity.class));
    }
}
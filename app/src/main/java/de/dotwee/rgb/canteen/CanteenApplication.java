package de.dotwee.rgb.canteen;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by lukas on 10.11.2016.
 */
public class CanteenApplication extends Application {
    private static final String TAG = CanteenApplication.class.getSimpleName();
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getStaticPreferences() {
        return sharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // Init timber
        Timber.plant(new Timber.DebugTree());

        // Init static preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}

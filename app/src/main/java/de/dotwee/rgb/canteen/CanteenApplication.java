package de.dotwee.rgb.canteen;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by lukas on 10.11.2016.
 */
public class CanteenApplication extends MultiDexApplication {
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

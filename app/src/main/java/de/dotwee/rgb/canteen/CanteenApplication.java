package de.dotwee.rgb.canteen;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

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

        initFabric();

        // Init static preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private void initFabric() {

        // Enable Fabric only in production builds
        CrashlyticsCore core = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();

        Fabric.with(this, new Crashlytics.Builder().core(core).build());
    }

}

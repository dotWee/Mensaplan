package de.dotwee.rgb.canteen;

import android.app.Application;

import timber.log.Timber;

public class MensaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}

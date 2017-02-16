package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.NonNull;

import java.io.File;

import de.dotwee.rgb.canteen.CanteenApplication;
import de.dotwee.rgb.canteen.model.api.cache.CacheHelper;
import de.dotwee.rgb.canteen.view.activities.SettingsView;

/**
 * Created by lukas on 19.12.2016.
 */
public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private final SettingsView settingsView;
    private final File cacheDir;

    public SettingsPresenterImpl(@NonNull SettingsView settingsView, @NonNull File cacheDir) {
        this.settingsView = settingsView;
        this.cacheDir = cacheDir;
    }

    @Override
    public void onClickPreferenceCacheClear() {
        CacheHelper.clear(cacheDir);
    }

    @Override
    public void onClickPreferenceCacheResetSettings() {

        // Delete all preferences
        CanteenApplication.getStaticPreferences()
                .edit()
                .clear()
                .apply();

        settingsView.finishView();
    }

}

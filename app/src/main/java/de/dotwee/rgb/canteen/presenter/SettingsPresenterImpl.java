package de.dotwee.rgb.canteen.presenter;

import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;
import java.util.Map;

import de.dotwee.rgb.canteen.CanteenApplication;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.helper.LicensesHelper;
import de.dotwee.rgb.canteen.view.activities.SettingsActivity;
import timber.log.Timber;

/**
 * Created by lukas on 19.12.2016.
 */
public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private final SettingsActivity settingsActivity;

    public SettingsPresenterImpl(@NonNull SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    @Override
    public void onSetCopyrightCategory(@NonNull PreferenceCategory preferenceCategory) {
        Map<Integer, List> licensesMap = LicensesHelper.getLicensesMap(settingsActivity);
        List listNames = licensesMap.get(LicensesHelper.LIST_NAMES);
        List listLicense = licensesMap.get(LicensesHelper.LIST_LICENSE);
        List listUrl = licensesMap.get(LicensesHelper.LIST_URL);

        if (listNames.size() == listLicense.size() && listLicense.size() == listUrl.size()) {
            for (int i = 0; i < listNames.size(); i++) {
                String name = (String) listNames.get(i);
                String license = (String) listLicense.get(i);
                final String url = (String) listUrl.get(i);

                Preference preference = getNewLicensePreference(name, license, url);
                preferenceCategory.addPreference(preference);
            }

        } else
            Timber.e("Sizes don't match names=%d licenses=%d urls=%d", listNames.size(), listLicense.size(), listUrl.size());
    }

    @NonNull
    private Preference getNewLicensePreference(@NonNull String name, @NonNull String license, @NonNull final String url) {
        Preference preference = new Preference(settingsActivity);
        preference.setTitle(name);
        preference.setSummary(license);

        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                settingsActivity.startActivity(intent);
                return true;
            }
        });

        return preference;
    }

    @Override
    public boolean onClickPreferenceCacheClear() {
        File cacheDir = settingsActivity.getCacheDir();
        CacheHelper.clear(cacheDir);
        return true;
    }

    @Override
    public boolean onClickPreferenceCacheResetSettings() {

        // Delete all preferences
        CanteenApplication.getStaticPreferences()
                .edit()
                .clear()
                .apply();

        settingsActivity.finish();

        return true;
    }

}

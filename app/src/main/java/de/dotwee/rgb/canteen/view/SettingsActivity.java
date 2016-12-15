package de.dotwee.rgb.canteen.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.CanteenApplication;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.helper.LicensesHelper;
import timber.log.Timber;

/**
 * Created by lukas on 27.11.2016.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private static final int[] KEYS = new int[]{
                R.string.preference_cache_reset_settings,
                R.string.preference_cache_clear,

                R.string.preference_appearance_switch_colorseparation,
                R.string.preference_appearance_list_price
        };

        @Override
        public Preference findPreference(CharSequence key) {
            return super.findPreference(key);
        }

        @Nullable
        public Preference findPreference(@StringRes int keyId) {
            String key = getString(keyId);
            return super.findPreference(key);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            setPreferenceClickListener();

            PreferenceCategory preferenceCategoryCopyright = (PreferenceCategory)
                    getPreferenceScreen().findPreference("preference_copyright");
            setCopyrightCategory(preferenceCategoryCopyright);
        }

        void setPreferenceClickListener() {
            for (int keyId : KEYS) {
                Preference preference = findPreference(keyId);
                if (preference != null) {
                    preference.setOnPreferenceClickListener(this);
                }
            }
        }

        void setCopyrightCategory(@NonNull PreferenceCategory preferenceCategoryCopyright) {
            Map<Integer, List> licensesMap = LicensesHelper.getLicensesMap(getActivity());
            List listNames = licensesMap.get(LicensesHelper.LIST_NAMES);
            List listLicense = licensesMap.get(LicensesHelper.LIST_LICENSE);
            List listUrl = licensesMap.get(LicensesHelper.LIST_URL);

            if (listNames.size() == listLicense.size() && listLicense.size() == listUrl.size()) {
                for (int i = 0; i < listNames.size(); i++) {
                    String name = (String) listNames.get(i);
                    String license = (String) listLicense.get(i);
                    final String url = (String) listUrl.get(i);

                    Preference preference = getNewLicensePreference(name, license, url);
                    preferenceCategoryCopyright.addPreference(preference);
                }

            } else
                Timber.e("Sizes don't match names=%d licenses=%d urls=%d", listNames.size(), listLicense.size(), listUrl.size());
        }

        @NonNull
        Preference getNewLicensePreference(@NonNull String name, @NonNull String license, @NonNull final String url) {
            Preference preference = new Preference(getActivity());
            preference.setTitle(name);
            preference.setSummary(license);

            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            });

            return preference;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            int keyId = getStringIdFromPreference(preference);

            switch (keyId) {

                case R.string.preference_cache_clear:
                    File cacheDir = getActivity().getCacheDir();
                    CacheHelper.clear(cacheDir);
                    return true;

                case R.string.preference_cache_reset_settings:
                    // Delete all preferences
                    CanteenApplication.getStaticPreferences().edit().clear().commit();

                    getActivity().finish();
                    return true;
            }

            return false;
        }

        @StringRes
        int getStringIdFromPreference(@NonNull Preference preference) {
            for (int keyId : KEYS) {
                String key = getString(keyId);
                String prefKey = preference.getKey();

                if (key.equalsIgnoreCase(prefKey)) {
                    return keyId;
                }
            }

            return 0;
        }
    }
}

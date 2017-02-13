package de.dotwee.rgb.canteen.view.activities;

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
import android.widget.FrameLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.helper.LicensesHelper;
import de.dotwee.rgb.canteen.presenter.SettingsPresenter;
import de.dotwee.rgb.canteen.presenter.SettingsPresenterImpl;
import timber.log.Timber;

/**
 * Created by lukas on 27.11.2016.
 */
public class SettingsActivity extends AppCompatActivity implements SettingsView {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static SettingsPresenter settingsPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        if (settingsPresenter == null) {
            settingsPresenter = new SettingsPresenterImpl(this, getCacheDir());
        }

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setSettingsFragment(settingsPresenter);
    }

    private void setSettingsFragment(@NonNull SettingsPresenter settingsPresenter) {
        SettingsFragment settingsFragment = new SettingsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, settingsPresenter);
        settingsFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, settingsFragment)
                .commit();
    }

    @Override
    public void onSetCopyrightCategory(@NonNull PreferenceCategory preferenceCategory) {
        Map<Integer, List> licensesMap = LicensesHelper.getLicensesMap(this);
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

        } else {
            Timber.e("Sizes don't match names=%d licenses=%d urls=%d", listNames.size(), listLicense.size(), listUrl.size());
        }
    }

    @Override
    public void finishView() {
        this.finish();
    }

    @NonNull
    private Preference getNewLicensePreference(@NonNull String name, @NonNull String license, @NonNull final String url) {
        Preference preference = new Preference(this);
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

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private static final int[] KEYS = new int[]{
                R.string.preference_cache_reset_settings,
                R.string.preference_cache_clear,

                R.string.preference_appearance_switch_colorseparation,
                R.string.preference_appearance_list_price
        };

        SettingsPresenter settingsPresenter;

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);

            this.settingsPresenter = (SettingsPresenter) args.get(SettingsActivity.TAG);
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
            ((SettingsView) getActivity()).onSetCopyrightCategory(preferenceCategoryCopyright);
        }

        void setPreferenceClickListener() {
            for (int keyId : KEYS) {
                Preference preference = findPreference(keyId);
                if (preference != null) {
                    preference.setOnPreferenceClickListener(this);
                }
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            int keyId = getStringIdFromPreference(preference);

            switch (keyId) {
                case R.string.preference_cache_clear:
                    settingsPresenter.onClickPreferenceCacheClear();
                    return true;

                case R.string.preference_cache_reset_settings:
                    settingsPresenter.onClickPreferenceCacheResetSettings();
                    return true;

                default:
                    return false;
            }
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

package de.dotwee.rgb.canteen.view.activities;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.presenter.SettingsPresenter;
import de.dotwee.rgb.canteen.presenter.SettingsPresenterImpl;
import timber.log.Timber;

/**
 * Created by lukas on 27.11.2016.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsPresenter = new SettingsPresenterImpl(this);
        setSettingsFragment(settingsPresenter);
    }

    private void setSettingsFragment(@NonNull SettingsPresenter settingsPresenter) {
        SettingsFragment settingsFragment = new SettingsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(SettingsPresenter.class.getName(), settingsPresenter);
        settingsFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, settingsFragment)
                .commit();
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

            this.settingsPresenter = (SettingsPresenter) args.get(SettingsPresenter.class.getName());
        }

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
            settingsPresenter.onSetCopyrightCategory(preferenceCategoryCopyright);
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
                    return settingsPresenter.onClickPreferenceCacheClear();

                case R.string.preference_cache_reset_settings:
                    return settingsPresenter.onClickPreferenceCacheResetSettings();

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

package de.dotwee.rgb.canteen.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.CanteenApplication;
import de.dotwee.rgb.canteen.R;
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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            setOnPreferenceClickListener();

            PreferenceCategory preferenceCategoryCopyright = (PreferenceCategory)
                    getPreferenceScreen().findPreference("preference_copyright");
            setCopyrightCategory(preferenceCategoryCopyright);
        }

        void setOnPreferenceClickListener() {
            Map<String, ?> keyMap = CanteenApplication.getStaticPreferences().getAll();
            for (Map.Entry<String, ?> entry : keyMap.entrySet()) {
                Preference preference = findPreference(entry.getKey());

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
            return false;
        }
    }
}

package de.dotwee.rgb.canteen.view.activities;

import android.preference.PreferenceCategory;
import android.support.annotation.NonNull;

/**
 * Created by lukas on 10.02.17.
 */
public interface SettingsView {
    static final String TAG = SettingsView.class.getSimpleName();

    void onSetCopyrightCategory(@NonNull PreferenceCategory preferenceCategory);

    void finishView();
}

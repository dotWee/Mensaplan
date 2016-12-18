package de.dotwee.rgb.canteen.presenter;

import android.preference.PreferenceCategory;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by lukas on 19.12.2016.
 */
public interface SettingsPresenter extends Serializable {

    void onSetCopyrightCategory(@NonNull PreferenceCategory preferenceCategory);

    boolean onClickPreferenceCacheClear();

    boolean onClickPreferenceCacheResetSettings();
}

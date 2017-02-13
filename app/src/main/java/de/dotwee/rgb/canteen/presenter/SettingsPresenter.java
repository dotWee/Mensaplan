package de.dotwee.rgb.canteen.presenter;

import java.io.Serializable;

/**
 * Created by lukas on 19.12.2016.
 */
public interface SettingsPresenter extends Serializable {

    void onClickPreferenceCacheClear();

    void onClickPreferenceCacheResetSettings();

}

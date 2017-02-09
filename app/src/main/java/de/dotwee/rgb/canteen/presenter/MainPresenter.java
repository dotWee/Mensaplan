package de.dotwee.rgb.canteen.presenter;

/**
 * Created by lukas on 09.02.17.
 */
public interface MainPresenter {
    String TAG = MainPresenter.class.getSimpleName();

    void onRefresh();

    void onLocationChange();

    void onWeekdayChange();
}

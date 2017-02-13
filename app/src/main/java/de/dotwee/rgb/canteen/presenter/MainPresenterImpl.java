package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.NonNull;

import java.io.File;

import de.dotwee.rgb.canteen.model.api.MealProvider;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.activities.MainView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lukas on 09.02.17.
 */
public class MainPresenterImpl implements MainPresenter, Observer<WeekMeal> {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();
    private final MainView mainView;
    private final MainView.SettingView settingView;
    private final File cacheDir;
    private DayMeal dayMeal = null;
    private boolean dataRefreshing = false;

    public MainPresenterImpl(@NonNull MainView mainView, @NonNull MainView.SettingView settingView, @NonNull File cacheDir) {
        this.mainView = mainView;
        this.settingView = settingView;
        this.cacheDir = cacheDir;
    }

    private void refreshVisibleMeal() {
        if (!dataRefreshing) {
            Timber.i("Perform data refresh");
            dataRefreshing = true;
            String locationTag = settingView.getSelectedLocation().getNameTag();

            MealProvider.getObservable(locationTag, DateHelper.getCurrentWeeknumber(), cacheDir)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        } else {
            Timber.i("Not performing refresh. View says refresh is already running!");
            onComplete();
        }
    }

    @Override
    public void onRefresh() {
        refreshVisibleMeal();
    }

    @Override
    public void onLocationChange() {
        refreshVisibleMeal();
    }

    @Override
    public void onWeekdayChange() {
        refreshVisibleMeal();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(WeekMeal weekMeal) {
        Timber.i("onNext WeekMeal");

        this.dataRefreshing = false;

        dayMeal = weekMeal.get(settingView.getSelectedWeekday());
        mainView.setDataset(dayMeal);
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);

        mainView.showSnackbar();
    }

    @Override
    public void onComplete() {
        Timber.i("onComplete");

        this.dataRefreshing = false;
        mainView.setRefreshing(false);

        boolean isDataAvailable = dayMeal != null && !dayMeal.isEmpty();
        mainView.showNoDataView(isDataAvailable);
    }
}

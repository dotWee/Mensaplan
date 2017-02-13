package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.NonNull;

import java.io.File;

import de.dotwee.rgb.canteen.model.api.MealProvider;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.activities.MainView;
import io.reactivex.Observable;
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
    private final Observable<WeekMeal> weekMealObservable;
    private boolean dataRefreshing = false;
    private DayMeal dayMeal = null;
    private WeekMeal weekMeal = null;

    public MainPresenterImpl(@NonNull MainView mainView, @NonNull MainView.SettingView settingView, @NonNull File cacheDir) {
        this.mainView = mainView;
        this.settingView = settingView;

        this.weekMealObservable = MealProvider.getObservable(settingView.getSelectedLocation().getNameTag(), DateHelper.getCurrentWeeknumber(), cacheDir)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread());

        onLocationChange();
    }

    @Override
    public void onLocationChange() {
        if (!dataRefreshing) {
            Timber.i("Perform data refresh");
            dataRefreshing = true;

            weekMealObservable.subscribe(this);
        } else {
            Timber.i("Not performing refresh. View says refresh is already running!");
        }
    }

    @Override
    public void onWeekdayChange() {
        if (weekMeal != null) {
            onNext(weekMeal);

        } else onLocationChange();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(WeekMeal weekMeal) {
        Timber.i("onNext");

        this.dataRefreshing = false;
        this.weekMeal = weekMeal;

        mainView.setRefreshing(false);

        boolean isDataAvailable = false;
        if (weekMeal != null) {
            dayMeal = weekMeal.get(settingView.getSelectedWeekday());

            if (dayMeal != null && !dayMeal.isEmpty()) {
                isDataAvailable = true;
            }
        }

        mainView.showNoDataView(isDataAvailable);
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
    }
}

package de.dotwee.rgb.canteen.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.adapter.LocationAdapter;
import de.dotwee.rgb.canteen.model.api.MealProvider;
import de.dotwee.rgb.canteen.model.api.specs.Meal;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lukas on 15.01.17.
 */
public class WeekActivity extends AppCompatActivity implements SingleObserver<Meal> {
    private static final String TAG = WeekActivity.class.getSimpleName();
    LocationAdapter locationAdapter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);

        setContentView(R.layout.actvitiy_week);
        ButterKnife.bind(this);

        // TODO allow custom weeknumber
        MealProvider.getObservable(DateHelper.getCurrentWeeknumber(), getCacheDir())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        // ignored
    }

    @Override
    public void onSuccess(Meal meal) {
        Timber.i("onSuccess meal-size=%d", meal.size());

        locationAdapter = new LocationAdapter(this, meal);
        viewPager.setAdapter(locationAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Timber.e(e);
        // todo handle exceptions
    }
}

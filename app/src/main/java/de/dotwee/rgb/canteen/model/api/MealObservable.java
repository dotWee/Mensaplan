package de.dotwee.rgb.canteen.model.api;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.InputStream;

import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import timber.log.Timber;

/**
 * Created by lukas on 02.01.17.
 */

public class MealObservable {
    private static final String TAG = MealObservable.class.getSimpleName();

    @NonNull
    public static Observable<WeekMeal> getObservable(@NonNull final String locationTag, final int weekOfYear, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<WeekMeal>() {
            @Override
            public void subscribe(ObservableEmitter<WeekMeal> e) throws Exception {
                long startMillis = System.currentTimeMillis();
                Timber.i("%s execution started", TAG);

                InputStream inputStream = MealProvider.getInputStream(cacheDir, locationTag, weekOfYear);
                if (inputStream != null) {
                    WeekMeal weekMeal = MealProvider.readWeekMenu(inputStream);
                    e.onNext(weekMeal);
                } else throw new IllegalStateException("InputStream is null");

                long endMillis = System.currentTimeMillis();
                Timber.i("%s execution ended | execution_time=%s milliseconds", TAG, endMillis - startMillis);
            }
        });
    }
}

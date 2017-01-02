package de.dotwee.rgb.canteen.model.api;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.InputStream;

import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import timber.log.Timber;

/**
 * Created by lukas on 19.11.2016.
 */
@Deprecated
public class MealRunnable implements Runnable {
    private static final String TAG = MealRunnable.class.getSimpleName();
    private final Receiver receiver;
    private final String locationTag;
    private final int weekOfYear;
    private final File cacheDir;

    @Deprecated
    public MealRunnable(Receiver receiver, String locationTag, int weekOfYear, File cacheDir) {
        this.receiver = receiver;
        this.locationTag = locationTag;
        this.weekOfYear = weekOfYear;
        this.cacheDir = cacheDir;
    }

    @Override
    public void run() {
        long startMillis = System.currentTimeMillis();
        Timber.i("%s execution started", TAG);

        try {
            InputStream inputStream = MealProvider.getInputStream(cacheDir, locationTag, weekOfYear);
            if (inputStream != null) {
                WeekMeal weekMeal = MealProvider.readWeekMenu(inputStream);
                receiver.onDataLoaded(weekMeal);
            } else receiver.onDataError(new IllegalStateException("InputStream is null"));

        } catch (Exception e) {
            e.printStackTrace();

            receiver.onDataError(e);
            Timber.e(e);
        }

        long endMillis = System.currentTimeMillis();
        Timber.i("%s execution ended | execution_time=%s milliseconds", TAG, endMillis - startMillis);
    }

    @Deprecated
    public interface Receiver {

        void onDataLoaded(@NonNull WeekMeal weekMeal);

        void onDataError(@NonNull Exception exception);
    }
}

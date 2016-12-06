package de.dotwee.rgb.canteen.model.api;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import de.dotwee.rgb.canteen.model.WeekMenu;
import timber.log.Timber;

/**
 * Created by lukas on 19.11.2016.
 */
public class MealRunnable implements Runnable {
    private static final String TAG = MealRunnable.class.getSimpleName();
    private final Receiver receiver;
    private final String locationTag;
    private final int weekOfYear;
    private final File cacheDir;

    public MealRunnable(Receiver receiver, String locationTag, int weekOfYear, File cacheDir) {
        this.receiver = receiver;
        this.locationTag = locationTag;
        this.weekOfYear = weekOfYear;
        this.cacheDir = cacheDir;
    }

    @Override
    public void run() {

        try {

            InputStream inputStream = MealProvider.getInputStream(cacheDir, locationTag, weekOfYear);

            if (inputStream != null) {
                WeekMenu weekMenu = MealProvider.readWeekMenu(inputStream);

                receiver.onDataLoaded(weekMenu);
            } else receiver.onDataError(new IllegalStateException("InputStream equals null"));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            Timber.e(e);
        }
    }

    public interface Receiver {

        void onDataLoaded(@NonNull WeekMenu weekMenu);

        void onDataError(@NonNull Throwable throwable);
    }
}

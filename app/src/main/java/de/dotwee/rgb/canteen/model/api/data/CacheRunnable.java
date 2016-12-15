package de.dotwee.rgb.canteen.model.api.data;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.threads.DefaultExecutorSupplier;
import timber.log.Timber;

import static de.dotwee.rgb.canteen.model.api.data.CacheHelper.FILENAME_FORMAT;

/**
 * Created by lukas on 15.12.2016.
 */
public class CacheRunnable implements Runnable {
    private static final String TAG = CacheRunnable.class.getSimpleName();

    private final int weeknumber;
    private final File cacheDir;
    private final Receiver receiver;

    public CacheRunnable(int weeknumber, @NonNull File cacheDir, @NonNull Receiver receiver) {
        this.weeknumber = weeknumber;
        this.cacheDir = cacheDir;
        this.receiver = receiver;

        DefaultExecutorSupplier.getInstance().executeBackgroundTask(this);
    }

    @Override
    public void run() {
        for (Location location : Location.values()) {
            Timber.i("Executing %s for location=%s weeknumber=%d", TAG, location.getNameTag(), weeknumber);

            String filename = String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber);
            if (!CacheHelper.exists(cacheDir, filename)) {
                InputStreamReceiver inputStreamReceiver = new InputStreamReceiver(cacheDir, filename);

                InputStreamDownloader inputStreamDownloader = new InputStreamDownloader(location.getNameTag(), weeknumber, inputStreamReceiver);
                inputStreamDownloader.populate();
            }
        }

        receiver.onFinished();
    }

    public interface Receiver {

        void onFinished();
    }
}

package de.dotwee.rgb.canteen.model.api.data;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import timber.log.Timber;

/**
 * Created by lukas on 15.12.2016.
 */
@Deprecated
class InputStreamReceiver {
    private static final String TAG = InputStreamReceiver.class.getSimpleName();
    private final File cacheDir;
    private final String filename;

    InputStreamReceiver(@NonNull File cacheDir, @NonNull String filename) {
        this.cacheDir = cacheDir;
        this.filename = filename;
    }

    void onStreamConnected(@NonNull HttpURLConnection httpURLConnection) {
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            CacheHelper.persist(cacheDir, inputStream, filename);
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e);
        }
    }

    void onConnectionError(@NonNull Exception exception) {
        // TODO handle connection issues
    }
}

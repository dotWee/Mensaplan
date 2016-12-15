package de.dotwee.rgb.canteen.model.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.threads.DefaultExecutorSupplier;
import timber.log.Timber;

/**
 * Created by lukas on 15.12.2016.
 */
class InputStreamDownloader implements Runnable {
    private static final String TAG = InputStreamDownloader.class.getSimpleName();

    private final String locationTag;
    private final int weeknumber;
    private InputStreamReceiver receiver;

    InputStreamDownloader(@NonNull String locationTag, int weeknumber, @NonNull InputStreamReceiver receiver) {
        this.locationTag = locationTag;
        this.weeknumber = weeknumber;
        this.receiver = receiver;
    }

    @Nullable
    private static HttpURLConnection getConnection(@NonNull URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();

        return httpURLConnection;
    }

    @NonNull
    private URL getUrl() throws MalformedURLException {
        String URL_FORMAT = "http://www.stwno.de/infomax/daten-extern/csv/%s/%s.csv";
        return new URL(String.format(Locale.getDefault(), URL_FORMAT, locationTag, String.valueOf(weeknumber)));
    }

    @Override
    public void run() {
        try {
            URL url = getUrl();
            HttpURLConnection httpURLConnection = getConnection(url);

            if (httpURLConnection != null && httpURLConnection.getResponseCode() == 200) {
                receiver.onStreamConnected(httpURLConnection);
            } else throw new IllegalStateException("Issue with HttpUrlConnection");
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e);

            receiver.onConnectionError(e);
        }
    }

    void populate() {
        DefaultExecutorSupplier.getInstance().executeBackgroundTask(this);
    }
}

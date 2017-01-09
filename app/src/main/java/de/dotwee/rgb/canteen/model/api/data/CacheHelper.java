package de.dotwee.rgb.canteen.model.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.constant.Location;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import timber.log.Timber;

/**
 * Created by lukas on 12.11.2016.
 */
public class CacheHelper {
    public static final String FILENAME_FORMAT = "%s-%d.csv";
    private static final String TAG = CacheHelper.class.getSimpleName();

    private static final String URL_FORMAT = "http://www.stwno.de/infomax/daten-extern/csv/%s/%s.csv";


    public static Location[] getCached(@NonNull File cacheDir, int weeknumber) {
        List<Location> locationList = new ArrayList<>();

        for (File file : cacheDir.listFiles()) {
            for (Location location : Location.values()) {
                String filename = String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber);
                if (file.getName().equalsIgnoreCase(filename)) {
                    locationList.add(location);
                }
            }
        }

        Location[] locations = new Location[locationList.size()];
        locations = locationList.toArray(locations);
        return locations;
    }

    public static void clear(@NonNull File cacheDir) {
        for (File file : cacheDir.listFiles()) {
            boolean deletion = file.delete();

            if (!deletion) {
                Timber.e("File with path=%s couldn't be deleted", file.getAbsolutePath());
            }
        }
    }

    static void persist(@NonNull File cacheDir, @NonNull InputStream inputStream, @NonNull String filename) {
        Timber.i("persisting file=%s", filename);

        OutputStream fileOutputStream = null;
        File file = new File(cacheDir, filename);

        try {
            // false == overwrite file
            fileOutputStream = new FileOutputStream(file, false);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, read);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Timber.e(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.e(e);
                }
            }
        }
    }

    @Nullable
    public static InputStream read(@NonNull File cacheDir, @NonNull String filename) throws FileNotFoundException {
        Timber.i("reading file=%s", filename);

        return new FileInputStream(new File(cacheDir, filename));
    }

    public static boolean exists(@NonNull File cacheDir, @NonNull String filename) {
        return new File(cacheDir, filename).exists();
    }

    @NonNull
    public static Observable<Location> getObservable(@NonNull final Location location, final int weeknumber, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<Location>() {
            @Override
            public void subscribe(ObservableEmitter<Location> e) throws Exception {
                HttpURLConnection httpURLConnection;
                String filename;
                URL url;

                Timber.i("Executing %s for location=%s weeknumber=%d", TAG, location.getNameTag(), weeknumber);

                // Declarate filename and url
                filename = String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber);
                url = new URL(String.format(Locale.getDefault(), URL_FORMAT, location.getNameTag(), String.valueOf(weeknumber)));

                // Connect to server
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                // If response code is fine > cache inputstream from connection
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    persist(cacheDir, inputStream, filename);
                    e.onNext(location);

                } else {
                    e.onError(new ConnectException(location.name()));
                }

                httpURLConnection.disconnect();
                e.onComplete();
            }
        });
    }
}

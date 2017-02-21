package de.dotwee.rgb.canteen.model.api.cache;

import android.support.annotation.NonNull;
import android.util.ArrayMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.dotwee.rgb.canteen.model.constant.Location;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by lukas on 16.02.17.
 */
public class CacheWrapper {
    private static final String TAG = CacheWrapper.class.getSimpleName();

    // %s = location TAG %d = weeknumber
    private static final String FILENAME_FORMAT = "%s-%d.csv";
    private final File cacheDir;

    public CacheWrapper(@NonNull File cacheDir) {
        this.cacheDir = cacheDir;
    }

    @NonNull
    private static String getFilename(@NonNull Location location, int weeknumber) {
        return String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber);
    }

    private static String getLocationFromFilename(@NonNull String filename) {
        return filename.split("-")[0];
    }

    private static int getWeeknumberFromFilename(@NonNull String filename) {
        return Integer.valueOf(filename.split("-")[1].split(".")[0]);
    }

    public static Observable<LinkedHashMap<Integer, File>> getObservable(@NonNull final File cacheDir, @NonNull final Location location /*, final int weeknumberOffset*/) {
        return Observable.create(new ObservableOnSubscribe<LinkedHashMap<Integer, File>>() {
            @Override
            public void subscribe(ObservableEmitter<LinkedHashMap<Integer, File>> e) throws Exception {
                Map<Integer, File> fileMap = new ArrayMap<>();

                for (File file : cacheDir.listFiles()) {
                    String filename = file.getName();
                    if (filename.contains(location.getNameTag())) {
                        int weeknumber = getWeeknumberFromFilename(filename);
                        fileMap.put(weeknumber, file);
                    }

                }

                /*
                int currentWeeknumber = DateHelper.getCurrentWeeknumber();
                for (int weeknumber = currentWeeknumber; weeknumber < currentWeeknumber + weeknumberOffset; weeknumber++) {
                    String filename = getFilename(location, weeknumber);
                    File file = new File(cacheDir, filename);
                    fileMap.put(weeknumber, file);
                }
                */

                e.onNext(new LinkedHashMap<Integer, File>(fileMap));
                e.onComplete();
            }
        });
    }

    /**
     * @return a list of all files inside the cache directory
     */
    @NonNull
    private List<File> getAllCacheFiles() {
        ArrayList<File> files = new ArrayList<>();
        Collections.addAll(files, cacheDir.listFiles());

        return files;
    }
}

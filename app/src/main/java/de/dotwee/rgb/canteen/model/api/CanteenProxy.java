package de.dotwee.rgb.canteen.model.api;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import timber.log.Timber;

/**
 * Created by lukas on 10.11.2016.
 */
public class CanteenProxy {
    public static final String FILENAME_FORMAT = "%s-%d.csv";
    private static final String TAG = CanteenProxy.class.getSimpleName();
    private static final String INDICATOR_DESSERT = "N";
    private static final String INDICATOR_MAIN = "HG";
    private static final String INDICATOR_SIDE_DISH = "B";
    private static final String INDICATOR_SOUP = "Suppe";
    private static final String URL_FORMAT = "http://www.stwno.de/infomax/daten-extern/csv/%s/%s.csv";
    private static final String EXCEPTION_FORMAT = "url=%s | responsecode=%d";

    public static void clear(@NonNull File cacheDir) {
        for (File file : cacheDir.listFiles()) {
            boolean deletion = file.delete();

            if (!deletion) {
                Timber.e("File with path=%s couldn't be deleted", file.getAbsolutePath());
            }
        }
    }

    private static void persist(@NonNull File cacheDir, @NonNull InputStream inputStream, @NonNull String filename) {
        Timber.i("persisting file=%s", filename);

        OutputStream fileOutputStream = null;
        File file = new File(cacheDir, filename);

        try {
            // false == overwrite file
            fileOutputStream = new FileOutputStream(file, false);

            int read;
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

    @Nullable
    private static InputStream getInputStream(File cacheDir, String locationTag, int weeknumber) {
        String filename = String.format(Locale.getDefault(), CacheHelper.FILENAME_FORMAT, locationTag, weeknumber);
        InputStream inputStream = null;

        // Try to use cache as source; return if not null
        if (CacheHelper.exists(cacheDir, filename)) {

            // Exists in cache...
            Timber.i("Found file in cache");

            inputStream = getCacheStream(cacheDir, filename);
            Timber.i("Success=%b", inputStream != null);
        }

        return inputStream;
    }

    @Nullable
    private static InputStream getCacheStream(@NonNull File cacheDir, @NonNull String filename) {
        InputStream inputStream = null;

        try {
            inputStream = CacheHelper.read(cacheDir, filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        return inputStream;
    }

    @NonNull
    private static WeekMeal readWeekMenu(@NonNull InputStream inputStream) throws ParseException, IOException {
        ArrayList<Item> items = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream, "windows-1252");

        // Skip first line
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            try {
                Item item = readItem(line);
                items.add(item);
            } catch (IllegalStateException e) {
                Timber.i("Issue with line=%s", line);
                e.printStackTrace();
                Timber.e(e);
            }
        }

        int i = 0;
        for (Item item : items) {
            if (item.getWeekday() == Weekday.FRIDAY) {
                i++;
            }
        }
        inputStream.close();

        return new WeekMeal(items);
    }

    @NonNull
    private static Item readItem(@NonNull String line) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        String[] lineSplit = line.split(";");
        Item item = new Item(lineSplit[3]);

        item.setDate(dateFormat.parse(lineSplit[0]));
        item.setTag(lineSplit[1]);
        item.setType(getType(lineSplit[2]));
        item.setLabels(getLabel(lineSplit[4]));
        item.setPriceAll(lineSplit[5]);
        item.setPriceStudent(lineSplit[6]);
        item.setPriceEmployee(lineSplit[7]);
        item.setPriceGuest(lineSplit[8]);

        item.setWeekday(DateHelper.getWeekday(item.getDate()));

        return item;
    }

    @NonNull
    private static Type getType(@NonNull String line) {

        if (line.contains(INDICATOR_DESSERT)) {
            return Type.DESSERT;
        } else if (line.contains(INDICATOR_SIDE_DISH)) {
            return Type.SIDE_DISH;
        } else if (line.contains(INDICATOR_SOUP)) {
            return Type.SOUP;
        } else {
            return Type.MAIN;
        }
    }

    @NonNull
    private static Label[] getLabel(@NonNull String line) {
        String[] labels = line.split(",");
        Label[] types = new Label[labels.length];
        int len = labels.length;

        for (int i = 0; i < len; i++) {
            try {
                types[i] = Label.valueOf(labels[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                types[i] = Label.NONE;
                Timber.e(e);
            }
        }
        return types;
    }

    @NonNull
    public static Observable<WeekMeal> getObservable(@NonNull final String locationTag, final int weekOfYear, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<WeekMeal>() {

            @Override
            public void subscribe(ObservableEmitter<WeekMeal> e) throws Exception {
                long startMillis = System.currentTimeMillis();
                Timber.i("%s execution started", TAG);

                InputStream inputStream = getInputStream(cacheDir, locationTag, weekOfYear);
                if (inputStream != null) {
                    WeekMeal weekMeal = readWeekMenu(inputStream);
                    e.onNext(weekMeal);
                } else e.onError(new Throwable("InputStream is null"));

                Timber.i("%s execution ended | execution_time=%s milliseconds | Calling onComplete", TAG, System.currentTimeMillis() - startMillis);
                e.onComplete();
            }
        });
    }

    @NonNull
    public static Observable<Location> getObservable(@NonNull final Location location, final int weeknumber, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<Location>() {
            @Override
            public void subscribe(ObservableEmitter<Location> e) throws Exception {
                Timber.i("Executing %s for location=%s weeknumber=%d", TAG, location.getNameTag(), weeknumber);

                // Declarate filename and url
                String filename = String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber);
                URL url = new URL(String.format(Locale.getDefault(), URL_FORMAT, location.getNameTag(), String.valueOf(weeknumber)));

                // Connect to server
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                int responseCode = httpURLConnection.getResponseCode();

                // If response code is fine, cache inputstream from connection
                if (responseCode == 200) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    persist(cacheDir, inputStream, filename);
                    e.onNext(location);

                } else {
                    e.onError(new ConnectException(String.format(Locale.getDefault(), EXCEPTION_FORMAT, url.toString(), responseCode)));
                }

                httpURLConnection.disconnect();
                e.onComplete();
            }
        });
    }
}

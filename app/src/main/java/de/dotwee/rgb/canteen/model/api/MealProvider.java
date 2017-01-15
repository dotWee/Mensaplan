package de.dotwee.rgb.canteen.model.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class MealProvider {
    private static final String TAG = MealProvider.class.getSimpleName();

    private static final String INDICATOR_DESSERT = "N";
    private static final String INDICATOR_MAIN = "HG";
    private static final String INDICATOR_SIDE_DISH = "B";
    private static final String INDICATOR_SOUP = "Suppe";

    @Nullable
    public static InputStream getInputStream(File cacheDir, String locationTag, int weeknumber) {
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
    static WeekMeal readWeekMenu(@NonNull InputStream inputStream) throws ParseException, IOException {
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
    public static Observable<WeekMeal> getObservable(final int weekOfYear, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<WeekMeal>() {
            @Override
            public void subscribe(ObservableEmitter<WeekMeal> e) throws Exception {
                long startMillis = System.currentTimeMillis();
                Timber.i("%s execution started", TAG);

                InputStream inputStream;
                for (Location location : Location.values()) {
                    inputStream = getInputStream(cacheDir, location.getNameTag(), weekOfYear);
                    if (inputStream != null) {
                        WeekMeal weekMeal = readWeekMenu(inputStream);
                        e.onNext(weekMeal);
                    } else e.onError(new IllegalStateException("InputStream is null"));
                }

                long endMillis = System.currentTimeMillis();
                Timber.i("%s execution ended | execution_time=%s milliseconds", TAG, endMillis - startMillis);
                e.onComplete();
            }
        });
    }

    @NonNull
    public static Observable<WeekMeal> getObservableForLocation(@NonNull final Location location, final int weekOfYear, @NonNull final File cacheDir) {
        return Observable.create(new ObservableOnSubscribe<WeekMeal>() {
            @Override
            public void subscribe(ObservableEmitter<WeekMeal> e) throws Exception {
                long startMillis = System.currentTimeMillis();
                Timber.i("%s execution started", TAG);

                InputStream inputStream = getInputStream(cacheDir, location.getNameTag(), weekOfYear);
                if (inputStream != null) {
                    WeekMeal weekMeal = readWeekMenu(inputStream);
                    e.onNext(weekMeal);
                } else e.onError(new IllegalStateException("InputStream is null"));

                long endMillis = System.currentTimeMillis();

                Timber.i("%s execution ended | execution_time=%s milliseconds", TAG, endMillis - startMillis);
                e.onComplete();
            }
        });
    }
}

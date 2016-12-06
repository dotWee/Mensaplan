package de.dotwee.rgb.canteen.model.api;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.WeekMenu;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.helper.CacheHelper;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import timber.log.Timber;

/**
 * Created by lukas on 10.11.2016.
 */
class MealProvider {
    private static final String TAG = MealProvider.class.getSimpleName();

    private static final String INDICATOR_DESSERT = "N";
    private static final String INDICATOR_MAIN = "HG";
    private static final String INDICATOR_SIDE_DISH = "B";
    private static final String INDICATOR_SOUP = "Suppe";

    @NonNull
    public static InputStream getInputStream(File cacheDir, String locationTag, int weeknumber) {
        String filename = locationTag + "-" + weeknumber + ".csv";
        InputStream inputStream = null;

        boolean existsInCache = CacheHelper.exists(cacheDir, filename);
        if (existsInCache) {
            try {
                inputStream = CacheHelper.read(cacheDir, filename);
                return inputStream;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Timber.e(e);
            }
        } else {
            String rawUrl = "http://www.stwno.de/infomax/daten-extern/csv/" + locationTag + "/" + weeknumber + ".csv";
            try {
                URL url = new URL(rawUrl);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();

                // Cache inputstream
                CacheHelper.persist(cacheDir, inputStream, filename);
                httpURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                Timber.e(e);
            }
        }

        if (inputStream == null) {
            return getInputStream(cacheDir, locationTag, weeknumber);
        }

        return inputStream;
    }

    @NonNull
    public static WeekMenu readWeekMenu(@NonNull InputStream inputStream) throws ParseException, IOException {
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

        return new WeekMenu(items);
    }

    @NonNull
    private static Item readItem(@NonNull String line) throws ParseException, UnsupportedEncodingException {
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
}

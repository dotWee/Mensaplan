package de.dotwee.rgb.canteen.model.helper;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import de.dotwee.rgb.canteen.R;
import timber.log.Timber;

/**
 * Created by lukas on 03.12.2016.
 */
public class LicensesHelper {
    public static final int LIST_NAMES = 0, LIST_LICENSE = 1, LIST_URL = 2;
    private static final String TAG = LicensesHelper.class.getSimpleName();

    @NonNull
    public static Map<Integer, List> getLicensesMap(@NonNull Context context) {
        Map<Integer, List> licensesMap = new ArrayMap<>();
        List<String> listNames = new ArrayList<>();
        List<String> listLicense = new ArrayList<>();
        List<String> listUrl = new ArrayList<>();

        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.licenses);

        Scanner scanner = new Scanner(inputStream);

        // Skip first line
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String rows[] = line.split(";");

            String name = rows[0];
            addName(listNames, name);

            String license = rows[1];
            addLicense(listLicense, license);

            String url = rows[2];
            addUrl(listUrl, url);
        }

        licensesMap.put(LIST_NAMES, listNames);
        licensesMap.put(LIST_LICENSE, listLicense);
        licensesMap.put(LIST_URL, listUrl);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        return licensesMap;
    }

    private static void addName(@NonNull List<String> listNames, @Nullable String name) {
        if (name == null) {
            Timber.e("Name is null!");
            return;
        }

        if (name.isEmpty()) {
            Timber.e("Name is empty!");
            return;
        }

        listNames.add(name);
    }

    private static void addLicense(@NonNull List<String> listLicenses, @Nullable String license) {
        if (license == null) {
            Timber.e("License is null!");
            return;
        }

        if (license.isEmpty()) {
            Timber.e("License is empty!");
            return;
        }

        listLicenses.add(license);
    }

    private static void addUrl(@NonNull List<String> listUrls, @Nullable String url) {
        if (url == null) {
            Timber.e("Url is null!");
            return;
        }

        if (url.isEmpty()) {
            Timber.e("Url is empty!");
            return;
        }

        listUrls.add(url);
    }
}

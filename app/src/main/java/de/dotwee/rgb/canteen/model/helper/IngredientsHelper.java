package de.dotwee.rgb.canteen.model.helper;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dotwee.rgb.canteen.R;
import timber.log.Timber;

/**
 * Created by lukas on 07.12.2016.
 */
public class IngredientsHelper {
    private static final String TAG = IngredientsHelper.class.getSimpleName();

    @RawRes
    private static final int RESOURCE_ID = R.raw.ingredients;

    private static final String KEY_BREAK = "<br>", KEY_WHITESPACE = " ", KEY_NEWLINE = "\n";

    @NonNull
    public static String getIngredientsContent(@NonNull Resources resources, @Nullable String itemInfo) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> itemInfoKeys = null;
        if (itemInfo != null) {
            itemInfoKeys = getIngredientsKeys(itemInfo);
        }

        try {
            JSONObject rawObject = getIngredientsJson(resources);

            Iterator<String> titleIterator = rawObject.keys();

            String ingredientsTitle = titleIterator.next();
            JSONObject ingredientsObject = rawObject.getJSONObject(ingredientsTitle);
            Map<String, String> ingredientsMap = getKeyValueMap(ingredientsObject, itemInfoKeys);
            stringBuilder.append(getContentString(ingredientsTitle, ingredientsMap));

            stringBuilder.append(KEY_BREAK).append(KEY_BREAK).append(KEY_NEWLINE);

            String allergensTitle = titleIterator.next();
            JSONObject allergensObject = rawObject.getJSONObject(allergensTitle);
            Map<String, String> allergensMap = getKeyValueMap(allergensObject, itemInfoKeys);
            stringBuilder.append(getContentString(allergensTitle, allergensMap));

        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        return stringBuilder.toString();
    }

    @NonNull
    private static List<String> getIngredientsKeys(@NonNull String itemInfo) {

        // Remove brackets
        String cleanedString = itemInfo.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        String splitString[] = cleanedString.split(",");

        List<String> strings = new ArrayList<>();
        Collections.addAll(strings, splitString);

        return strings;
    }

    @NonNull
    private static String getContentString(@NonNull String title, @NonNull Map<String, String> contentMap) {
        StringBuilder stringBuilder = new StringBuilder();

        // Create title
        stringBuilder.append("<strong>").append(title).append("</strong>").append(KEY_NEWLINE);

        // Add content
        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            stringBuilder.append(KEY_BREAK)
                    .append(entry.getKey())
                    .append(KEY_WHITESPACE)
                    .append(entry.getValue())
                    .append(KEY_NEWLINE);
        }

        return stringBuilder.toString();
    }

    @NonNull
    private static Map<String, String> getKeyValueMap(@NonNull JSONObject jsonObject, @Nullable List<String> only) {
        Iterator<String> keyIterator = jsonObject.keys();
        Map<String, String> keyValueMap = new ArrayMap<>();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            if (only != null) {
                for (String string : only) {
                    if (string.equalsIgnoreCase(key)) {
                        String value = jsonObject.optString(key);
                        keyValueMap.put(key, value);
                    }
                }
            } else {
                String value = jsonObject.optString(key);
                keyValueMap.put(key, value);
            }
        }

        return keyValueMap;
    }

    @NonNull
    private static JSONObject getIngredientsJson(@NonNull Resources resources) throws JSONException {
        InputStream inputStream = resources.openRawResource(RESOURCE_ID);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new JSONObject(sb.toString());
    }
}

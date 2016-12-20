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
    private static final String KEY_BREAK = "<br>", KEY_WHITESPACE = " ";
    public static String KEYS_ALL = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,g,x,y,z,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17";

    @NonNull
    public static String getIngredientsContent(@NonNull Resources resources, @Nullable String itemInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            JSONObject rawObject = getJson(resources, R.raw.ingredients);
            Iterator<String> titleIterator = rawObject.keys();

            String ingredientsTitle = titleIterator.next();
            JSONObject ingredientsObject = rawObject.getJSONObject(ingredientsTitle);

            List<String> itemInfoKeys = itemInfo == null ? null : getKeys(itemInfo);
            Map<String, String> ingredientsMap = getKeyValueMap(ingredientsObject, itemInfoKeys);
            if (ingredientsMap.size() != 0) {
                stringBuilder.append(getContentString(ingredientsMap));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        return stringBuilder.toString();
    }

    @NonNull
    public static String getAllergensContent(@NonNull Resources resources, @Nullable String itemInfo) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            JSONObject rawObject = getJson(resources, R.raw.allergens);
            Iterator<String> titleIterator = rawObject.keys();

            String allergensTitle = titleIterator.next();
            JSONObject allergensObject = rawObject.getJSONObject(allergensTitle);

            List<String> itemInfoKeys = itemInfo == null ? null : getKeys(itemInfo);
            Map<String, String> allergensMap = getKeyValueMap(allergensObject, itemInfoKeys);

            if (allergensMap.size() != 0) {
                stringBuilder.append(getContentString(allergensMap));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e(e);
        }

        return stringBuilder.toString();
    }

    @NonNull
    private static List<String> getKeys(@NonNull String itemInfo) {

        // Remove brackets
        String cleanedString = itemInfo.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        String splitString[] = cleanedString.split(",");

        List<String> strings = new ArrayList<>();
        Collections.addAll(strings, splitString);

        return strings;
    }

    @NonNull
    private static String getContentString(@NonNull Map<String, String> contentMap) {
        StringBuilder stringBuilder = new StringBuilder();

        // Add content
        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            stringBuilder
                    .append(entry.getKey())
                    .append(KEY_WHITESPACE)
                    .append(entry.getValue())
                    .append(KEY_BREAK);
        }

        return stringBuilder.toString();
    }

    @NonNull
    private static Map<String, String> getKeyValueMap(@NonNull JSONObject jsonObject, @Nullable List<String> only) {
        Iterator<String> keyIterator = jsonObject.keys();
        Map<String, String> keyValueMap = new ArrayMap<>();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = jsonObject.optString(key);

            if (only != null) {
                for (String string : only) {
                    if (string.equalsIgnoreCase(key)) {
                        keyValueMap.put(key, value);
                    }
                }
            } else {
                keyValueMap.put(key, value);
            }
        }

        return keyValueMap;
    }

    @NonNull
    private static JSONObject getJson(@NonNull Resources resources, @RawRes int resId) throws JSONException {
        InputStream inputStream = resources.openRawResource(resId);

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

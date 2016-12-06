package de.dotwee.rgb.canteen.model.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import de.dotwee.rgb.canteen.CanteenApplication;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.constant.Price;

/**
 * Created by lukas on 30.11.2016.
 */
public class PreferencesHelper {
    private static final String TAG = PreferencesHelper.class.getSimpleName();

    private PreferencesHelper() {
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public static String getStringPreference(String key) {
        String value = null;
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setStringPreference(String key, String value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    /**
     * Helper method to retrieve a float value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static float getFloatPreference(String key, float defaultValue) {
        float value = defaultValue;
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a float value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setFloatPreference(String key, float value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    /**
     * Helper method to retrieve a long value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static long getLongPreference(String key, long defaultValue) {
        long value = defaultValue;
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a long value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setLongPreference(String key, long value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    /**
     * Helper method to retrieve an integer value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static int getIntegerPreference(String key, int defaultValue) {
        int value = defaultValue;
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write an integer value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setIntegerPreference(String key, int value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    /**
     * Helper method to retrieve a boolean value from {@link SharedPreferences}.
     *
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public static boolean getBooleanPreference(String key, boolean defaultValue) {
        boolean value = defaultValue;
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a boolean value to {@link SharedPreferences}.
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public static boolean setBooleanPreference(String key, boolean value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
            return true;
        }
        return false;
    }

    public static <T extends Enum<T>> T getEnumValue(Class<T> type, String key, T defaultValue) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        String name = preferences.getString(key, null);
        if (name != null) {
            try {
                return Enum.valueOf(type, name);
            } catch (IllegalArgumentException ignored) {
                // Ignore
            }
        }

        return defaultValue;
    }

    public static void saveEnumValue(String key, Enum value) {
        SharedPreferences preferences = CanteenApplication.getStaticPreferences();
        preferences.edit().putString(key, value.name()).apply();
    }

    public static boolean isColorSeparationEnabled(@NonNull Context context) {
        String key = context.getString(R.string.preference_appearance_switch_colorseparation);
        return getBooleanPreference(key, true);
    }

    public static Price getPriceAppearance(@NonNull Context context) {
        String key = context.getString(R.string.preference_appearance_list_price);
        String def = getStringPreference(key);
        if (def == null) {
            return Price.STUDENT;
        } else if (def.equalsIgnoreCase(Price.ALL.toString())) {
            return Price.ALL;
        } else if (def.equalsIgnoreCase(Price.EMPLOYEE.toString())) {
            return Price.EMPLOYEE;
        } else if (def.equalsIgnoreCase(Price.GUEST.toString())) {
            return Price.GUEST;
        } else return Price.STUDENT;
    }
}
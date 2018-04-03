package ankurkhandelwal.example.com.pulseemotionadrenaline;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {

    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences("prefs", 0);

    }

    private static SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public static String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defVal) {
        return prefs.getBoolean(key, defVal);
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public static void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public static void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }
}

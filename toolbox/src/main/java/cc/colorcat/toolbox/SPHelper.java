package cc.colorcat.toolbox;

import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cxx on 2017/9/6.
 * xx.ch@outlook.com
 */
public class SPHelper {
    private static final Map<String, Object> CACHE = new WeakHashMap<>();
    private static Bridge bridge;

    public static void init(Bridge bridge) {
        if (bridge == null) throw new NullPointerException("bridge == null");
        SPHelper.bridge = bridge;
    }

    public static <T> T read(String key, Class<T> clz) {
        T result = getCached(key);
        if (result == null) {
            String json = bridge.getSharedPreferences().getString(key, null);
            if (json != null) {
                result = bridge.fromJson(json, clz);
                if (result != null) {
                    CACHE.put(key, result);
                }
            }
        }
        return result;
    }

    public static <T> T read(String key, Type typeOfT) {
        T result = getCached(key);
        if (result == null) {
            String json = bridge.getSharedPreferences().getString(key, null);
            if (json != null) {
                result = bridge.fromJson(json, typeOfT);
                if (result != null) {
                    CACHE.put(key, result);
                }
            }
        }
        return result;
    }

    public static void save(String key, Object obj) {
        if (obj != null) {
            CACHE.put(key, obj);
            String json = bridge.toJson(obj);
            SharedPreferences.Editor editor = bridge.getSharedPreferences().edit();
            editor.putString(key, json);
            editor.apply();
        }
    }

    public static void remove(String key) {
        CACHE.remove(key);
        SharedPreferences.Editor editor = bridge.getSharedPreferences().edit();
        editor.remove(key);
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getCached(String key) {
        return (T) CACHE.get(key);
    }

    private SPHelper() {
        throw new AssertionError("no instance");
    }

    public interface Bridge {

        SharedPreferences getSharedPreferences();

        <T> T fromJson(String json, Class<T> clz);

        <T> T fromJson(String json, Type typeOfT);

        String toJson(Object obj);
    }
}

package cc.colorcat.toolbox;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cxx on 16-3-8.
 * xx.ch@outlook.com
 */
public final class L {
    private static final String TAG = "Temp";
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;
    private static final int NOTHING = 10;
    private static int level = NOTHING;

    public static void init(boolean debug) {
        level = debug ? VERBOSE : NOTHING;
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        l(tag, msg, VERBOSE);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        l(tag, msg, DEBUG);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        l(tag, msg, INFO);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        l(tag, msg, WARN);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        l(tag, msg, ERROR);
    }

    public static void e(Throwable e) {
        if (ERROR >= level) {
            e.printStackTrace();
        }
    }

    public static void l(String tag, String msg, @Level int level) {
        if (level >= L.level) {
            Log.println(level, tag, msg);
        }
    }

    public static void ll(String tag, String msg, @Level int level) {
        Log.println(level, tag, msg);
    }

    public static void ll(String msg, @Level int level) {
        Log.println(level, TAG, msg);
    }

    private L() {
        throw new AssertionError("no instance.");
    }

    @IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
    }
}
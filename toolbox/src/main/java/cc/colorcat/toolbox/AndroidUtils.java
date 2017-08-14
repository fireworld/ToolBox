package cc.colorcat.toolbox;

import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxx on 17-8-14.
 * xx.ch@outlook.com
 */
public class AndroidUtils {

    public static void setChildViewEnabled(ViewGroup group, boolean enabled) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View v = group.getChildAt(i);
            v.setEnabled(enabled);
            if (v instanceof ViewGroup) {
                setChildViewEnabled((ViewGroup) v, enabled);
            }
        }
    }

    public static void setChildViewEnabled(ViewGroup group, boolean enabled, @IdRes int... ignores) {
        for (int i = 0, count = group.getChildCount(); i < count; i++) {
            View v = group.getChildAt(i);
            int id = v.getId();
            if (!contains(ignores, id)) {
                v.setEnabled(enabled);
            }
            if (v instanceof ViewGroup) {
                setChildViewEnabled((ViewGroup) v, enabled, ignores); // 递归
            }
        }
    }

    private static boolean contains(int[] ints, int i) {
        for (int e : ints) {
            if (e == i) {
                return true;
            }
        }
        return false;
    }

    private AndroidUtils() {
        throw new AssertionError("no instance");
    }
}

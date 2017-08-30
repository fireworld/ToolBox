package cc.colorcat.toolbox;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * Created by cxx on 17-8-14.
 * xx.ch@outlook.com
 */
public class AndroidUtils {

    public static void setFullScreen(Activity activity) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        Window window = activity.getWindow();
        // Sets the color of the status bar and navigation bar to transparent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

    public static void setFullScreenExcludeNavBar(Activity activity) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        Window window = activity.getWindow();
        // Sets the color of the status bar and navigation bar to transparent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

    public static Drawable buildSelected(Drawable normal, Drawable selected) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected}, selected);
        drawable.addState(new int[]{-android.R.attr.state_selected}, normal);
        return drawable;
    }

    public static ColorStateList buildSelected(@ColorInt int normal, @ColorInt int selected) {
        int[][] states = {
                {android.R.attr.state_selected},
                {-android.R.attr.state_selected}
        };
        int[] colors = {selected, normal};
        return new ColorStateList(states, colors);
    }

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

    private static void fixInputManagerLeak(Context ctx) {
        InputMethodManager manager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) return;
        Class clz = manager.getClass();
        String[] fieldNames = {"mCurRootView", "mServedView", "mNextServedView"};
        for (String name : fieldNames) {
            try {
                Field field = clz.getDeclaredField(name);
                field.setAccessible(true);
                Object viewObject = field.get(manager);
                if (viewObject instanceof View) {
                    View view = (View) viewObject;
                    if (view.getContext() == ctx) {
                        field.set(manager, null);
                    }
                }
            } catch (Exception ignore) {
                L.e(ignore);
            }
        }
    }

    private AndroidUtils() {
        throw new AssertionError("no instance");
    }
}

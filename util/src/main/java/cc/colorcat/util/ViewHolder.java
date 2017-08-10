package cc.colorcat.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class ViewHolder {
    private final SparseArray<View> views = new SparseArray<>();
    protected final View root;
    private Integer viewType;

    public static ViewHolder from(@NonNull Activity activity) {
        return new ViewHolder(activity.getWindow().getDecorView());
    }

    public static ViewHolder from(@NonNull LayoutInflater inflater, @LayoutRes int resId) {
        return new ViewHolder(inflater.inflate(resId, null));
    }

    public static ViewHolder from(@NonNull LayoutInflater inflater, @LayoutRes int resId, ViewGroup root) {
        return from(inflater, resId, root, false);
    }

    public static ViewHolder from(@NonNull LayoutInflater inflater, @LayoutRes int resId, ViewGroup root, boolean attachToRoot) {
        return new ViewHolder(inflater.inflate(resId, root, attachToRoot));
    }

    public static ViewHolder from(@NonNull Context context, @LayoutRes int resId) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resId, null));
    }

    public static ViewHolder from(@LayoutRes int resId, @NonNull ViewGroup root) {
        return from(resId, root, false);
    }

    public static ViewHolder from(@LayoutRes int resId, @NonNull ViewGroup root, boolean attachToRoot) {
        return new ViewHolder(LayoutInflater.from(root.getContext()).inflate(resId, root, attachToRoot));
    }

    public static ViewHolder from(@NonNull View root) {
        return new ViewHolder(root);
    }

    protected ViewHolder(View root) {
        if (root == null) {
            throw new NullPointerException("root == null");
        }
        this.root = root;
    }

    public View getRoot() {
        return root;
    }

    public ViewHolder setViewType(int type) {
        viewType = type;
        return this;
    }

    public int getViewType() {
        if (viewType == null) {
            throw new IllegalStateException("The viewType has never been set.");
        }
        return viewType;
    }

    @SuppressWarnings(value = "unchecked")
    public <V extends View> V getView(@IdRes int viewResId) {
        View view = views.get(viewResId);
        if (view == null) {
            view = root.findViewById(viewResId);
            if (view != null) {
                views.put(viewResId, view);
            } else {
                throw new NullPointerException("Can't find view, viewResId = " + viewResId);
            }
        }
        return (V) view;
    }

    public ViewHolder setTag(@IdRes int viewResId, final Object tag) {
        getView(viewResId).setTag(tag);
        return this;
    }

    public ViewHolder setTag(@IdRes int viewResId, int key, final Object tag) {
        getView(viewResId).setTag(key, tag);
        return this;
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getTag(@IdRes int viewResId) {
        return (T) getView(viewResId).getTag();
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getTag(@IdRes int viewResId, int key) {
        return (T) getView(viewResId).getTag(key);
    }

    public ViewHolder setText(@IdRes int viewResId, CharSequence text) {
        TextView view = getView(viewResId);
        view.setText(text);
        return this;
    }

    public ViewHolder setText(@IdRes int viewResId, @StringRes int stringResId) {
        TextView view = getView(viewResId);
        view.setText(stringResId);
        return this;
    }

    public CharSequence getText(@IdRes int viewResId) {
        TextView view = getView(viewResId);
        return view.getText();
    }
}

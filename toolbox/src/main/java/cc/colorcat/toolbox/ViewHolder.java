package cc.colorcat.toolbox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class ViewHolder {
    private final SparseArray<View> mViews = new SparseArray<>();
    protected final View mRoot;

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
            throw new NullPointerException("mRoot == null");
        }
        mRoot = root;
    }

    public View getRoot() {
        return mRoot;
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V optView(@IdRes int viewResId) {
        View view = mViews.get(viewResId);
        if (view == null) {
            view = mRoot.findViewById(viewResId);
            if (view != null) {
                mViews.put(viewResId, view);
            }
        }
        return (V) view;
    }

    /**
     * @throws NullPointerException if can not find the view by viewResId.
     */
    @SuppressWarnings(value = "unchecked")
    public <V extends View> V getView(@IdRes int viewResId) {
        View view = mViews.get(viewResId);
        if (view == null) {
            view = mRoot.findViewById(viewResId);
            if (view != null) {
                mViews.put(viewResId, view);
            } else {
                throw new NullPointerException("Can't find view, viewResId = " + viewResId);
            }
        }
        return (V) view;
    }

    public ViewHolder setOnClickListener(@IdRes int viewResId, View.OnClickListener listener) {
        getView(viewResId).setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnClickListener(View.OnClickListener listener, @IdRes int... viewResIds) {
        for (int resId : viewResIds) {
            getView(resId).setOnClickListener(listener);
        }
        return this;
    }

    public ViewHolder setOnLongClickListener(@IdRes int viewResId, View.OnLongClickListener listener) {
        getView(viewResId).setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(View.OnLongClickListener listener, @IdRes int... viewResIds) {
        for (int resId : viewResIds) {
            getView(resId).setOnLongClickListener(listener);
        }
        return this;
    }

    public ViewHolder setVisibility(@IdRes int viewResId, @Visibility int visibility) {
        getView(viewResId).setVisibility(visibility);
        return this;
    }

    @Visibility
    public int getVisibility(@IdRes int viewResId) {
        return getView(viewResId).getVisibility();
    }

    public ViewHolder setEnabled(@IdRes int viewResId, boolean enabled) {
        getView(viewResId).setEnabled(enabled);
        return this;
    }

    public boolean isEnabled(@IdRes int viewResId) {
        return getView(viewResId).isEnabled();
    }

    public ViewHolder setPadding(@IdRes int viewResId, int padding) {
        View view = getView(viewResId);
        view.setPadding(padding, padding, padding, padding);
        return this;
    }

    public ViewHolder setPadding(@IdRes int viewResId, int left, int top, int right, int bottom) {
        View view = getView(viewResId);
        view.setPadding(left, top, right, bottom);
        return this;
    }

    public ViewHolder setBackground(@IdRes int viewResId, Drawable background) {
        View view = getView(viewResId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        }
        return this;
    }

    public ViewHolder setBackground(@IdRes int viewResId, @DrawableRes int resId) {
        getView(viewResId).setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setBackgroundColor(@IdRes int viewResId, @ColorInt int color) {
        getView(viewResId).setBackgroundColor(color);
        return this;
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

    public ViewHolder setText(@IdRes int viewResId, @StringRes int resId) {
        TextView view = getView(viewResId);
        view.setText(resId);
        return this;
    }

    public CharSequence getText(@IdRes int viewResId) {
        TextView view = getView(viewResId);
        return view.getText();
    }

    public String getString(@IdRes int viewResId) {
        return getText(viewResId).toString();
    }

    public String getTrimmedString(@IdRes int viewResId) {
        return getText(viewResId).toString().trim();
    }

    public ViewHolder setError(@IdRes int viewResId, CharSequence error) {
        TextView view = getView(viewResId);
        view.setError(error);
        return this;
    }

    public ViewHolder setError(@IdRes int viewResId, @StringRes int resId) {
        TextView view = getView(viewResId);
        CharSequence tip = view.getResources().getText(resId);
        view.setError(tip);
        return this;
    }

    public ViewHolder setError(@IdRes int viewResId, CharSequence error, Drawable icon) {
        TextView view = getView(viewResId);
        view.setError(error, icon);
        return this;
    }

    public ViewHolder setError(@IdRes int viewResId, @StringRes int resId, Drawable icon) {
        TextView view = getView(viewResId);
        CharSequence tip = view.getResources().getText(resId);
        view.setError(tip, icon);
        return this;
    }

    /**
     * Sets flags on the Paint being used to display the text and
     * reflows the text if they are different from the old flags.
     *
     * @see android.graphics.Paint#setFlags(int)
     */
    public ViewHolder setPaintFlags(@IdRes int viewResId, int flags) {
        TextView view = getView(viewResId);
        view.setPaintFlags(flags);
        return this;
    }

    public int getPaintFlags(@IdRes int viewResId) {
        TextView view = getView(viewResId);
        return view.getPaintFlags();
    }

    public ViewHolder setChecked(@IdRes int viewResId, boolean checked) {
        Checkable checkable = getView(viewResId);
        checkable.setChecked(checked);
        return this;
    }

    public boolean isChecked(@IdRes int viewResId) {
        Checkable checkable = getView(viewResId);
        return checkable.isChecked();
    }

    public ViewHolder toggle(@IdRes int viewResId) {
        Checkable checkable = getView(viewResId);
        checkable.toggle();
        return this;
    }

    public ViewHolder setOnCheckedChangeListener(@IdRes int viewResId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton cb = getView(viewResId);
        cb.setOnCheckedChangeListener(listener);
        return this;
    }

    public ViewHolder setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener, @IdRes int... viewResIds) {
        for (int resId : viewResIds) {
            CompoundButton cb = getView(resId);
            cb.setOnCheckedChangeListener(listener);
        }
        return this;
    }

    public ViewHolder setImageResource(@IdRes int viewResId, @DrawableRes int resId) {
        ImageView view = getView(viewResId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageDrawable(@IdRes int viewResId, Drawable drawable) {
        ImageView view = getView(viewResId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageBitmap(@IdRes int viewResId, File file) {
        setImageBitmap(viewResId, file.getAbsolutePath());
        return this;
    }

    public ViewHolder setImageBitmap(@IdRes int viewResId, String pathName) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        setImageBitmap(viewResId, bitmap);
        return this;
    }

    public ViewHolder setImageBitmap(@IdRes int viewResId, Bitmap bitmap) {
        ImageView view = getView(viewResId);
        view.setImageBitmap(bitmap);
        return this;
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}
}

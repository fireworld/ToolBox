package cc.colorcat.toolbox.widget;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.colorcat.toolbox.R;

/**
 * Created by cxx on 2018/2/24.
 * xx.ch@outlook.com
 */
public class Tip {
    public static Tip from(@NonNull Activity activity, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        View content = parent.getChildAt(0);
        return new Tip(parent, content, tipLayout, listener);
    }

    public static Tip from(Fragment fragment, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        View content = fragment.getView();
        if (content == null) {
            throw new NullPointerException("fragment.getView() == null");
        }
        ViewGroup parent = (ViewGroup) content.getParent();
        return new Tip(parent, parent.getChildAt(0), tipLayout, listener);
    }

    public static Tip from(android.support.v4.app.Fragment fragment, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        View content = fragment.getView();
        if (content == null) {
            throw new NullPointerException("fragment.getView() == null");
        }
        ViewGroup parent = (ViewGroup) content.getParent();
        return new Tip(parent, parent.getChildAt(0), tipLayout, listener);
    }

    public static Tip from(DialogFragment dialogFragment, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        View content = dialogFragment.getView();
        if (content == null) {
            throw new NullPointerException("dialogFragment.getView() == null");
        }
        ViewGroup parent = (ViewGroup) content.getParent();
        return new Tip(parent, parent.getChildAt(0), tipLayout, listener);
    }

    public static Tip from(android.support.v4.app.DialogFragment dialogFragment, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        View content = dialogFragment.getView();
        if (content == null) {
            throw new NullPointerException("dialogFragment.getView() == null");
        }
        ViewGroup parent = (ViewGroup) content.getParent();
        return new Tip(parent, parent.getChildAt(0), tipLayout, listener);
    }

    public static Tip from(View view, @LayoutRes int tipLayout, @Nullable Tip.Listener listener) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            throw new NullPointerException("The specified view must have a parent");
        }
        return new Tip(parent, view, tipLayout, listener);
    }

    private final ViewGroup mParentView;
    private View mContentView;
    private int mContentIndex;
    private View mTipView;
    @LayoutRes
    private int mTipLayout;
    private Listener mListener;

    private Tip(ViewGroup parent, View contentView, @LayoutRes int tipLayout, Listener listener) {
        mParentView = parent;
        mContentView = contentView;
        mTipLayout = tipLayout;
        mListener = listener;
    }

    public final void showTip() {
        final View tipView = getTipView();
        if (tipView.getParent() == null) {
            mContentIndex = mParentView.indexOfChild(mContentView);
            mParentView.removeViewAt(mContentIndex);
            mParentView.addView(tipView, mContentIndex, mContentView.getLayoutParams());
        }
    }

    public final void hideTip() {
        if (mTipView != null && mContentView.getParent() == null) {
            mParentView.removeView(mTipView);
            mParentView.addView(mContentView, mContentIndex);
        }
    }

    public void setListener(@Nullable Tip.Listener listener) {
        mListener = listener;
    }

    private View getTipView() {
        if (mTipView == null) {
            mTipView = LayoutInflater.from(mParentView.getContext()).inflate(mTipLayout, mParentView, false);
            View tip = mTipView.findViewById(R.id.tip);
            if (tip != null) {
                tip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideTip();
                        if (mListener != null) {
                            mListener.onTipClick();
                        }
                    }
                });
            }
        }
        return mTipView;
    }

    public interface Listener {
        void onTipClick();
    }
}

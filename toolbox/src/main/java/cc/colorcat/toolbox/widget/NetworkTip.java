package cc.colorcat.toolbox.widget;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.colorcat.toolbox.R;

/**
 * Created by cxx on 2018/2/24.
 * xx.ch@outlook.com
 */
public class NetworkTip {
    public static NetworkTip from(@NonNull Activity activity, OnRetryListener listener) {
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        return new NetworkTip(root, R.layout.tip_network_error, listener);
    }

    public static NetworkTip from(@NonNull Activity activity, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        return new NetworkTip(root, networkErrorLayout, listener);
    }

    public static NetworkTip from(Fragment fragment, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, R.layout.tip_network_error, listener);
    }

    public static NetworkTip from(Fragment fragment, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, networkErrorLayout, listener);
    }

    public static NetworkTip from(android.support.v4.app.Fragment fragment, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, R.layout.tip_network_error, listener);
    }

    public static NetworkTip from(android.support.v4.app.Fragment fragment, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, networkErrorLayout, listener);
    }

    public static NetworkTip from(DialogFragment fragment, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, R.layout.tip_network_error, listener);
    }

    public static NetworkTip from(DialogFragment fragment, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, networkErrorLayout, listener);
    }

    public static NetworkTip from(android.support.v4.app.DialogFragment fragment, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, R.layout.tip_network_error, listener);
    }

    public static NetworkTip from(android.support.v4.app.DialogFragment fragment, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        View view = fragment.getView();
        if (view == null) throw new IllegalArgumentException("fragment.getView() == null");
        ViewGroup root = (ViewGroup) view.getParent();
        return new NetworkTip(root, networkErrorLayout, listener);
    }

    private final ViewGroup mRoot;
    private View mContentView;
    private View mNetworkErrorView;
    @LayoutRes
    private int mNetworkErrorLayout;
    private OnRetryListener mListener;

    private NetworkTip(ViewGroup root, @LayoutRes int networkErrorLayout, OnRetryListener listener) {
        mRoot = root;
        mNetworkErrorLayout = networkErrorLayout;
        mListener = listener;
    }

    public final void showNetworkError() {
        mRoot.removeView(getContentView());
        final View networkErrorView = getNetworkErrorView();
        if (networkErrorView.getParent() == null) {
            mRoot.addView(networkErrorView);
        }
    }

    public final void restore() {
        mRoot.removeView(mNetworkErrorView);
        final View contentView = getContentView();
        if (contentView.getParent() == null) {
            mRoot.addView(contentView);
        }
    }

    private View getContentView() {
        if (mContentView == null) {
            mContentView = mRoot.getChildAt(0);
        }
        return mContentView;
    }

    private View getNetworkErrorView() {
        if (mNetworkErrorView == null) {
            mNetworkErrorView = LayoutInflater.from(mRoot.getContext()).inflate(mNetworkErrorLayout, mRoot, false);
            mNetworkErrorView.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restore();
                    if (mListener != null) {
                        mListener.onRetry();
                    }
                }
            });
        }
        return mNetworkErrorView;
    }
}

package cc.colorcat.toolbox.widget;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cxx on 16-6-17.
 * xx.ch@outlook.com
 */
public abstract class VpAdapter<T> extends PagerAdapter {
    private List<T> mData;
    private SparseArray<View> mViews;
    private OnItemLongClickListener mListener;

    public VpAdapter(@NonNull List<T> data) {
        mData = data;
        mViews = new SparseArray<>(mData.size());
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mListener = listener;
    }

    @Override
    public final int getCount() {
        return mData.size();
    }

    @Override
    public final Object instantiateItem(ViewGroup container, final int position) {
        container.addView(getView(container, position));
        return position;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == mViews.get((int) object);
    }

    @CallSuper
    @Override
    public void notifyDataSetChanged() {
        mViews.clear();
        super.notifyDataSetChanged();
    }

    private View getView(ViewGroup container, final int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = getView(container, mData.get(position));
            if (mListener != null) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mListener.onItemLongClick(v, position);
                        return false;
                    }
                });
            }
            mViews.put(position, view);
        }
        return view;
    }

    @NonNull
    protected abstract View getView(ViewGroup container, T data);

    public interface OnItemLongClickListener {
        void onItemLongClick(@NonNull View v, int position);
    }
}

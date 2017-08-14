package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;

import java.util.List;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public abstract class SimpleLvAdapter<T> extends LvAdapter {
    private final List<T> mData;
    @LayoutRes
    private final int mLayoutResId;

    public SimpleLvAdapter(List<T> data, @LayoutRes int resId) {
        mData = data;
        mLayoutResId = resId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    @LayoutRes
    public int getLayoutResId(int viewType) {
        return mLayoutResId;
    }

    @Override
    public void inflateData(LvHolder holder, int position) {
        bindView(holder, getItem(position));
    }

    public abstract void bindView(LvHolder holder, T t);
}

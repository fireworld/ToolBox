package cc.colorcat.util.widget;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public abstract class SimpleLvAdapter<T> extends BaseAdapter {
    private final List<T> mData;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        LvHolder holder = LvHolder.getHolder(position, viewType, convertView, parent, mLayoutResId);
        bindView(holder, getItem(position));
        return holder.getRoot();
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    public abstract void bindView(LvHolder holder, T t);
}

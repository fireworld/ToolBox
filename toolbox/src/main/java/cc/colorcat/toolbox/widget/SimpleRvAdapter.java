package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;

import java.util.List;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public abstract class SimpleRvAdapter<T> extends RvAdapter {
    private final List<T> mData;
    private final int mLayoutResId;

    public SimpleRvAdapter(List<T> data, @LayoutRes int layoutResId) {
        mData = data;
        mLayoutResId = layoutResId;
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getLayoutResId(int viewType) {
        return mLayoutResId;
    }

    @Override
    public void bindView(RvHolder holder, int position) {
        bindView(holder, mData.get(position));
    }

    public abstract void bindView(RvHolder holder, T data);
}

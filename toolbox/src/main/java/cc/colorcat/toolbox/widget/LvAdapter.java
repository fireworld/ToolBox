package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public abstract class LvAdapter extends BaseAdapter {

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        int layoutResId = getLayoutResId(viewType);
        LvHolder holder = LvHolder.getHolder(position, viewType, convertView, parent, layoutResId);
        bindView(holder, position);
        return holder.getRoot();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(LvHolder holder, int position);
}

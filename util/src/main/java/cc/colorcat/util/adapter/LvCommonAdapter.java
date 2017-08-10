package cc.colorcat.util.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public abstract class LvCommonAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        int layoutResId = getLayoutResId(position, viewType);
        LvHolder holder = LvHolder.getHolder(position, viewType, convertView, parent, layoutResId);
        inflateData(holder);
        return holder.getRoot();
    }

    @LayoutRes
    public abstract int getLayoutResId(int position, int viewType);

    public abstract void inflateData(LvHolder holder);
}

package cc.colorcat.util.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.colorcat.util.ViewHolder;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class LvHolder extends ViewHolder {
    private int mPosition;

    private LvHolder(int position, int viewType, @NonNull ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        mPosition = position;
        mViewType = viewType;
    }

    public int getPosition() {
        return mPosition;
    }

    static LvHolder getHolder(int position, int viewType, View convertView, ViewGroup parent, @LayoutRes int resId) {
        LvHolder holder;
        if (convertView == null) {
            holder = new LvHolder(position, viewType, parent, resId);
            holder.getRoot().setTag(holder);
        } else {
            holder = (LvHolder) convertView.getTag();
            holder.mPosition = position;
            holder.mViewType = viewType;
        }
        return holder;
    }
}

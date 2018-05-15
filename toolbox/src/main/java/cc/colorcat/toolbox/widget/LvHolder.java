package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class LvHolder extends AdapterViewHolder {

    private LvHolder(@NonNull ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    static LvHolder getHolder(View convertView, ViewGroup parent, @LayoutRes int resId) {
        LvHolder holder;
        if (convertView == null) {
            holder = new LvHolder(parent, resId);
            holder.getRoot().setTag(holder);
        } else {
            holder = (LvHolder) convertView.getTag();
        }
        return holder;
    }
}

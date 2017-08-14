package cc.colorcat.toolbox.widget;

import android.view.View;

import cc.colorcat.toolbox.ViewHolder;

/**
 * Created by cxx on 17-8-14.
 * xx.ch@outlook.com
 */
public class AdapterViewHolder extends ViewHolder {
    protected int mViewType;
    protected int mPosition;

    protected AdapterViewHolder(View root) {
        super(root);
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
}

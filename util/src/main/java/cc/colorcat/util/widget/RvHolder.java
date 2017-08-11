package cc.colorcat.util.widget;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cc.colorcat.util.ViewHolder;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public final class RvHolder extends RecyclerView.ViewHolder {
    private final Holder mHolder;

    public RvHolder(View itemView, @Nullable SimpleRvAdapter.OnItemLongClickListener listener) {
        super(itemView);
        mHolder = new Holder(itemView);
        if (listener != null) {
            itemView.setOnLongClickListener(new RvOnLongClickListener(listener));
        }
    }

    public Holder getHolder() {
        return mHolder;
    }

    private class RvOnLongClickListener implements View.OnLongClickListener {
        private SimpleRvAdapter.OnItemLongClickListener mListener;

        RvOnLongClickListener(SimpleRvAdapter.OnItemLongClickListener listener) {
            mListener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongClick(v, getLayoutPosition());
            return false;
        }
    }

    public static class Holder extends ViewHolder {
        private int mPosition;

        private Holder(View root) {
            super(root);
        }

        void setPosition(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }
    }
}

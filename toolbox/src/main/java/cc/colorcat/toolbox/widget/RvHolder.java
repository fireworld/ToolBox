package cc.colorcat.toolbox.widget;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public final class RvHolder extends RecyclerView.ViewHolder {
    private final Helper mHelper;

    public RvHolder(View itemView, @Nullable RvAdapter.OnItemLongClickListener listener) {
        super(itemView);
        mHelper = new Helper(itemView);
        if (listener != null) {
            itemView.setOnLongClickListener(new RvOnLongClickListener(listener));
        }
    }

    public Helper getHelper() {
        return mHelper;
    }

    private class RvOnLongClickListener implements View.OnLongClickListener {
        private RvAdapter.OnItemLongClickListener mListener;

        RvOnLongClickListener(RvAdapter.OnItemLongClickListener listener) {
            mListener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongClick(v, getLayoutPosition());
            return false;
        }
    }

    public static class Helper extends AdapterViewHolder {

        private Helper(View root) {
            super(root);
        }
    }
}

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

    public RvHolder(View itemView) {
        super(itemView);
        mHelper = new Helper(itemView);
    }

    public RvHolder(View itemView, @Nullable RvAdapter.OnItemLongClickListener longClickListener, @Nullable RvAdapter.OnItemClickListener clickListener) {
        super(itemView);
        mHelper = new Helper(itemView);
        if (longClickListener != null) {
            itemView.setOnLongClickListener(new RvOnLongClickListener(longClickListener));
        }
        if (clickListener != null) {
            itemView.setOnClickListener(new RvOnClickListener(clickListener));
        }
    }

    public Helper getHelper() {
        return mHelper;
    }

    private class RvOnLongClickListener implements View.OnLongClickListener {
        private RvAdapter.OnItemLongClickListener mLongClickListener;

        RvOnLongClickListener(RvAdapter.OnItemLongClickListener longClickListener) {
            mLongClickListener = longClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            mLongClickListener.onItemLongClick(v, getLayoutPosition());
            return false;
        }
    }


    private class RvOnClickListener implements View.OnClickListener {
        private RvAdapter.OnItemClickListener mClickListener;

        RvOnClickListener(RvAdapter.OnItemClickListener clickListener) {
            mClickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(v, getLayoutPosition());
        }
    }


    public static class Helper extends AdapterViewHolder {

        private Helper(View root) {
            super(root);
        }
    }
}

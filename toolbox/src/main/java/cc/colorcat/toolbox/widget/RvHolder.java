package cc.colorcat.toolbox.widget;

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
        mHelper = new Helper(this);
    }

    public Helper getHelper() {
        return mHelper;
    }

    public static class Helper extends AdapterViewHolder {
        private RvHolder mHolder;

        private Helper(RvHolder holder) {
            super(holder.itemView);
            mHolder = holder;
        }

        public RecyclerView.ViewHolder getViewHolder() {
            return mHolder;
        }

        public int getPosition() {
            return mHolder.getAdapterPosition();
        }

        public int getViewType() {
            return mHolder.getItemViewType();
        }
    }
}

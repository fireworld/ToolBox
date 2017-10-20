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

    public Helper getHelper() {
        return mHelper;
    }

    public static class Helper extends AdapterViewHolder {

        private Helper(View root) {
            super(root);
        }
    }
}

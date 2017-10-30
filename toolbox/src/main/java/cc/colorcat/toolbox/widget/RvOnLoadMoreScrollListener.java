package cc.colorcat.toolbox.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cxx on 17-6-30.
 * xx.ch@outlook.com
 */
public abstract class RvOnLoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private boolean mUpOnLast = false;

    public RvOnLoadMoreScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int total = manager.getItemCount();
        View view = manager.findViewByPosition(total - 1);
        if (mUpOnLast && view != null && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mUpOnLast = dy > 0;
    }

    public abstract void onLoadMore();
}

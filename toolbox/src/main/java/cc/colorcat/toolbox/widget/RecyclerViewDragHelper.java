package cc.colorcat.toolbox.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by cxx on 18-2-8.
 * xx.ch@outlook.com
 */
public class RecyclerViewDragHelper extends ItemTouchHelper.Callback {
    private RecyclerView mRecyclerView;
    private List<?> mData;

    public RecyclerViewDragHelper(List<?> data) {
        mData = data;
    }

    public void attach(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        final ItemTouchHelper helper = new ItemTouchHelper(this);
        helper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnRvItemClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder holder) {
                super.onItemLongClick(holder);
                if (canDragOnLongPress(holder.getAdapterPosition())) {
                    helper.startDrag(holder);
                }
            }
        });
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int swipeFlags = 0;
        int drawFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            drawFlags |= ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }
        return makeMovementFlags(drawFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition(), to = target.getAdapterPosition();
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyItemMoved(from, to);
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public final boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return canDropOver(current.getAdapterPosition(), target.getAdapterPosition());
    }

    protected boolean canDropOver(int currentPosition, int targetPosition) {
        return true;
    }

    protected boolean canDragOnLongPress(int position) {
        return true;
    }
}

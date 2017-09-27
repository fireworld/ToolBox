package cc.colorcat.toolbox.widget;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cxx on 2017/9/14.
 * xx.ch@outlook.com
 */
public class OnRvItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView mRecyclerView;

    @Override
    public final boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mRecyclerView = rv;
        dispatchTouchEvent(e);
        return false;
    }

    @Override
    public final void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mRecyclerView = rv;
        dispatchTouchEvent(e);
    }

    private void dispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetectorCompat(
                    mRecyclerView.getContext(),
                    new ItemTouchHelperGestureListener()
            );
        }
        mGestureDetector.onTouchEvent(event);
    }

    @Override
    public final void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            RecyclerView.ViewHolder holder = findViewHolder(e);
            if (holder != null) {
                onItemClick(holder);
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            RecyclerView.ViewHolder holder = findViewHolder(e);
            if (holder != null) {
                onItemLongClick(holder);
            }
            super.onLongPress(e);
        }
    }

    private RecyclerView.ViewHolder findViewHolder(MotionEvent event) {
        if (mRecyclerView != null) {
            View childView = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
            if (childView != null) {
                return mRecyclerView.getChildViewHolder(childView);
            }
        }
        return null;
    }

    public void onItemClick(RecyclerView.ViewHolder holder) {

    }

    public void onItemLongClick(RecyclerView.ViewHolder holder) {

    }
}

package cc.colorcat.toolbox.widget;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2017/11/30.
 * xx.ch@outlook.com
 */
public abstract class AutoChoiceRvAdapter extends ChoiceRvAdapter {
    private static final String TAG = "AutoChoice";
    private List<Boolean> mRecord = new ArrayList<>();

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final int size = getItemCount();
        if (size > 0) {
            mRecord.clear();
            mRecord.addAll(createList(Boolean.FALSE, size));
        }
        registerAdapterDataObserver(mObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecord.clear();
        unregisterAdapterDataObserver(mObserver);
    }

    @Override
    protected boolean isSelected(int position) {
        return super.isSelected(position) || mRecord.get(position);
    }

    @Override
    protected void updateItem(int position, boolean selected) {
        super.updateItem(position, selected);
        mRecord.set(position, selected);
        Log.w(TAG, "updateItem, " + mRecord.toString());
    }

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.i(TAG, "onChanged");
            mRecord.clear();
            mRecord.addAll(createList(Boolean.FALSE, getItemCount()));
            Log.d(TAG, mRecord.toString());
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            Log.i(TAG, "onItemRangeChanged, start = " + positionStart + ", count = " + itemCount);
            for (int i = positionStart, end = positionStart + itemCount; i < end; i++) {
                mRecord.set(i, Boolean.FALSE);
            }
            Log.d(TAG, mRecord.toString());
        }

//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
//            super.onItemRangeChanged(positionStart, itemCount, payload);
//        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            Log.i(TAG, "onItemRangeInserted, start = " + positionStart + ", count = " + itemCount);
            mRecord.addAll(positionStart, createList(Boolean.FALSE, itemCount));
            Log.d(TAG, mRecord.toString());
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            Log.i(TAG, "onItemRangeRemoved, start = " + positionStart + ", count = " + itemCount);
            removeRange(positionStart, itemCount);
            Log.d(TAG, mRecord.toString());
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            Log.i(TAG, "from = " + fromPosition + ", to = " + toPosition + ", count = " + itemCount);
            List<Boolean> subList = mRecord.subList(fromPosition, fromPosition + itemCount);
            removeRange(fromPosition, itemCount);
            mRecord.addAll(toPosition, subList);
            Log.d(TAG, mRecord.toString());
        }

        private void removeRange(int start, int count) {
            for (int i = start + count - 1; i >= start; i--) {
                mRecord.remove(i);
            }
        }
    };

    private static List<Boolean> createList(Boolean defaultValue, int size) {
        List<Boolean> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(defaultValue);
        }
        return result;
    }
}

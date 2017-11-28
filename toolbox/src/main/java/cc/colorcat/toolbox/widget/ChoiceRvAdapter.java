package cc.colorcat.toolbox.widget;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cxx on 2017/8/13.
 * xx.ch@outlook.com
 */
public abstract class ChoiceRvAdapter extends RvAdapter {
    @ChoiceMode
    private int mChoiceMode = ChoiceMode.NONE;
    private int mSelectedPosition = AdapterView.INVALID_POSITION;
    private OnItemSelectedChangedListener mSelectedListener;
    private RecyclerView mRecyclerView;
    private SelectHelper mSelectHelper;

    @Override
    public final void onBindViewHolder(RvHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (inChoiceMode()) {
            setSelected(holder.itemView, isSelectedWithChoiceMode(position));
        }
//        final RvHolder.Helper helper = holder.getHelper();
//        helper.setViewType(holder.getItemViewType()).setPosition(position);
//        if (inChoiceMode()) {
//            setSelected(helper.getRoot(), isSelectedWithChoiceMode(position));
//        }
//        bindView(holder, position);
    }

    private boolean isSelectedWithChoiceMode(int position) {
        if (mChoiceMode == ChoiceMode.SINGLE) {
            return mSelectedPosition == position;
        }
        return isSelected(position);
    }

    public void setOnItemSelectedListener(OnItemSelectedChangedListener listener) {
        mSelectedListener = listener;
    }

    public OnItemSelectedChangedListener getOnItemSelectedChangedListener() {
        return mSelectedListener;
    }

    public void setChoiceMode(@ChoiceMode int choiceMode) {
        if (choiceMode == ChoiceMode.NONE
                || choiceMode == ChoiceMode.SINGLE
                || choiceMode == ChoiceMode.MULTIPLE) {
            mChoiceMode = choiceMode;
        } else {
            throw new IllegalArgumentException("Illegal choiceMode, value = " + choiceMode);
        }
    }

    public void attachForChoice(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mSelectHelper == null) {
            mSelectHelper = new SelectHelper();
        }
        mRecyclerView.addOnItemTouchListener(mSelectHelper);
    }

    protected void setSelected(View itemView, boolean selected) {
        itemView.setSelected(selected);
    }

    public void setSelection(int position) {
        if (inChoiceMode()
                && checkPosition(position)
                && (mChoiceMode == ChoiceMode.MULTIPLE || position != mSelectedPosition)
                && !isSelectedWithChoiceMode(position)) {
            dispatchSelect(position, true);
        }
    }

    public int getSelection() {
        return mSelectedPosition;
    }

    private void dispatchSelect(int position, boolean selected) {
        if (mChoiceMode == ChoiceMode.SINGLE) {
            if (selected) {
                final int last = mSelectedPosition;
                mSelectedPosition = position;
                if (checkPosition(last)) {
                    dispatchSelect(last, false);
                }
                notifySelectedChanged(mSelectedPosition, true);
            } else {
                notifySelectedChanged(position, false);
            }
        } else {
            notifySelectedChanged(position, selected);
        }
    }

    private void notifySelectedChanged(int position, boolean selected) {
        setSelected(position, selected);
        if (mRecyclerView != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.itemView.setSelected(selected);
            }
        } else {
            notifyItemChanged(position);
        }
        if (mSelectedListener != null) {
            mSelectedListener.onItemSelectedChanged(position, selected);
        }
    }

    public boolean isSelected(int position) {
        return false;
    }

    public void setSelected(int position, boolean selected) {

    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(RvHolder holder, int position);


    private boolean checkPosition(int position) {
        return position >= 0 && position < getItemCount();
    }

    private boolean inChoiceMode() {
        return mChoiceMode == ChoiceMode.SINGLE || mChoiceMode == ChoiceMode.MULTIPLE;
    }


    private class SelectHelper extends OnRvItemClickListener {
        @Override
        public void onItemClick(RecyclerView.ViewHolder holder) {
            super.onItemClick(holder);
            if (inChoiceMode()) {
                final int position = holder.getAdapterPosition();
                if (mChoiceMode == ChoiceMode.MULTIPLE || mSelectedPosition != position) {
                    dispatchSelect(position, !isSelectedWithChoiceMode(position));
                }
            }
        }
    }


    public interface OnItemSelectedChangedListener {

        void onItemSelectedChanged(int position, boolean selected);
    }


    @IntDef({ChoiceMode.NONE, ChoiceMode.SINGLE, ChoiceMode.MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChoiceMode {
        /**
         * Does not indicate choices
         */
        int NONE = 0;

        /**
         * Allows up to one choice
         */
        int SINGLE = 1;

        /**
         * Allows multiple choices
         */
        int MULTIPLE = 2;
    }
}

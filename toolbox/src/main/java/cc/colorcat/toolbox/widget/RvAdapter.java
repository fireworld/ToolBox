package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by cxx on 2017/8/13.
 * xx.ch@outlook.com
 */
public abstract class RvAdapter extends RecyclerView.Adapter<RvHolder> {
    private int mSelectedPosition = AdapterView.INVALID_POSITION;
    private OnItemSelectedListener mSelectedListener;

    @Override
    public final RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false);
        RvHolder holder = new RvHolder(itemView);
        holder.getHelper().setViewType(viewType).setPosition(holder.getAdapterPosition());
        return holder;
    }

    @Override
    public final void onBindViewHolder(RvHolder holder, int position) {
        RvHolder.Helper helper = holder.getHelper();
        helper.setViewType(holder.getItemViewType()).setPosition(position);
        helper.getRoot().setSelected(mSelectedPosition == position);
        bindView(holder, position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mSelectedListener = listener;
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return mSelectedListener;
    }

    public void setSelection(int position) {
        if (mSelectedPosition != position && position >= 0 && position < getItemCount()) {
            int last = mSelectedPosition;
            mSelectedPosition = position;
            notifyItemChanged(last);
            notifyItemChanged(mSelectedPosition);
            if (mSelectedListener != null) {
                mSelectedListener.onItemSelected(position);
            }
        }
    }

    public int getSelection() {
        return mSelectedPosition;
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(RvHolder holder, int position);


    public interface OnItemSelectedListener {

        void onItemSelected(int position);
    }
}

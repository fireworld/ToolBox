package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxx on 2017/8/13.
 * xx.ch@outlook.com
 */
public abstract class RvAdapter extends RecyclerView.Adapter<RvHolder> {
    private OnItemLongClickListener mLongClick;
    private OnItemClickListener mClick;

    @Override
    public final RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false);
        RvHolder holder = new RvHolder(itemView, mLongClick, mClick);
        holder.getHelper().setViewType(viewType);
        return holder;
    }

    @Override
    public final void onBindViewHolder(RvHolder holder, int position) {
        RvHolder.Helper helper = holder.getHelper();
        helper.setViewType(holder.getItemViewType());
        helper.setPosition(position);
        bindView(holder, position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClick = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mClick;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClick = listener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mLongClick;
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);


    public abstract void bindView(RvHolder holder, int position);


    public interface OnItemLongClickListener {

        void onItemLongClick(View view, int position);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}

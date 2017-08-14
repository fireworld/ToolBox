package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public abstract class SimpleRvAdapter<T> extends RecyclerView.Adapter<RvHolder> {
    private final int mLayoutResId;
    private final List<T> mData;
    private OnItemLongClickListener mListener;

    public SimpleRvAdapter(List<T> data, @LayoutRes int layoutResId) {
        mData = data;
        mLayoutResId = layoutResId;
    }

    @Override
    public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);
        RvHolder holder = new RvHolder(itemView, mListener);
        holder.getHelper().setViewType(viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RvHolder holder, int position) {
        RvHolder.Helper helper = holder.getHelper();
        helper.setViewType(holder.getItemViewType());
        helper.setPosition(position);
        bindView(helper, mData.get(position));
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public final int getItemCount() {
        return mData.size();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mListener = listener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mListener;
    }

    public abstract void bindView(RvHolder.Helper helper, T t);

    public interface OnItemLongClickListener {

        void onItemLongClick(View view, int position);
    }
}

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
    private SimpleRvAdapter.OnItemLongClickListener mListener;

    @Override
    public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false);
        RvHolder holder = new RvHolder(itemView, mListener);
        holder.getHelper().setViewType(viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RvHolder holder, int position) {
        RvHolder.Helper helper = holder.getHelper();
        helper.setViewType(holder.getItemViewType());
        helper.setPosition(position);
        bindView(helper, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(RvHolder.Helper helper, int position);

    public interface OnItemLongClickListener {

        void onItemLongClick(View view, int position);
    }
}

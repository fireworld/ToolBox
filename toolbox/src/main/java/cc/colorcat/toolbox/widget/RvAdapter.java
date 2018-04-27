package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxx on 2017/8/13.
 * xx.ch@outlook.com
 */
public abstract class RvAdapter extends RecyclerView.Adapter<RvHolder> {
    @NonNull
    @Override
    public final RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResId(viewType), parent, false);
        RvHolder holder = new RvHolder(itemView);
        holder.getHelper().setViewType(viewType).setPosition(holder.getAdapterPosition());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {
        holder.getHelper().setViewType(holder.getItemViewType()).setPosition(position);
        bindView(holder, position);
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(@NonNull RvHolder holder, int position);
}

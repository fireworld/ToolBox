package cc.colorcat.toolbox.widget;

import android.support.annotation.LayoutRes;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2017/11/28.
 * xx.ch@outlook.com
 */
public abstract class FixedSimpleChoiceRvAdapter<T> extends SimpleChoiceRvAdapter<T> {
    private SparseBooleanArray mRecord = new SparseBooleanArray();

    public FixedSimpleChoiceRvAdapter(List<T> data, @LayoutRes int layoutResId) {
        super(new ArrayList<>(data), layoutResId);
    }

    @Override
    public boolean isSelected(int position) {
        return super.isSelected(position) || mRecord.get(position, false);
    }

    @Override
    public final void updateItem(int position, boolean selected) {
        super.updateItem(position, selected);
        mRecord.put(position, selected);
    }
}

package cc.colorcat.toolbox;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by cxx on 2017/9/7.
 * xx.ch@outlook.com
 */
public class FixedSizeList<T> extends ArrayList<T> {
    private final int maxSize;

    public FixedSizeList(int maxSize) {
        super(maxSize + 1);
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize < 1");
        }
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {
        boolean result = super.add(t);
        fixSize();
        return result;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        fixSize();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean result = super.addAll(c);
        fixSize();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean result = super.addAll(index, c);
        fixSize();
        return result;
    }

    private void fixSize() {
        while (size() > maxSize) {
            remove(0);
        }
    }
}

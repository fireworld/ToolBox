package cc.colorcat.toolbox.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by cxx on 17-9-20.
 * xx.ch@outlook.com
 */
public class Flow<T> {
    private List<T> original;

    public static <T> Flow<T> with(List<T> data) {
        if (data == null) {
            return new Flow<>();
        }
        return new Flow<>(data);

    }

    private Flow(List<T> data) {
        original = new ArrayList<>(data);
    }

    private Flow() {
        original = new ArrayList<>();
    }

    public Flow<T> concat(Collection<T> data) {
        if (data != null && !data.isEmpty()) {
            original.addAll(data);
        }
        return this;
    }

    public Flow<T> filter(Filter<? super T> filter) {
        int size = original.size();
        List<T> newData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            T t = original.get(i);
            if (filter.filter(t)) {
                newData.add(t);
            }
        }
        return Flow.with(newData);
    }

    public boolean exists(Filter<T> filter) {
        for (int i = 0, size = original.size(); i < size; i++) {
            if (filter.filter(original.get(i))) {
                return true;
            }
        }
        return false;
    }

    public Flow<T> forEach(Consumer<? super T> consumer) {
        for (int i = 0, size = original.size(); i < size; i++) {
            consumer.accept(original.get(i));
        }
        return this;
    }

    public <V> Flow<V> map(Function<? super T, ? extends V> function) {
        int size = original.size();
        List<V> newData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            newData.add(function.apply(original.get(i)));
        }
        return Flow.with(newData);
    }

    public Flow<T> onNext(Next<T> next) {
        next.onNext(new ArrayList<>(original));
        return this;
    }

    public Flow<T> skipEmpty(Next<T> next) {
        if (!original.isEmpty()) {
            next.onNext(new ArrayList<>(original));
        }
        return this;
    }
}

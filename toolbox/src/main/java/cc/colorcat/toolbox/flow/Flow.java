package cc.colorcat.toolbox.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cxx on 17-9-20.
 * xx.ch@outlook.com
 */
public class Flow<T> {
    private List<T> original;

    public static <T> Flow<T> create() {
        return new Flow<>();
    }

    public static <T> Flow<T> with(T t) {
        return new Flow<>(t);
    }

    public static <T> Flow<T> with(T... ts) {
        return new Flow<>(Arrays.asList(ts));
    }

    public static <T> Flow<T> with(Creator<T> creator) {
        List<T> data = creator.create();
        if (data == null) {
            return new Flow<>();
        }
        return new Flow<>(data);
    }

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

    private Flow(T t) {
        original = new ArrayList<>();
        original.add(t);
    }

    public Flow<T> concat(Collection<T> data) {
        if (data != null && !data.isEmpty()) {
            original.addAll(data);
        }
        return this;
    }

    public Flow<T> concat(T data) {
        if (data != null) {
            original.add(data);
        }
        return this;
    }

    public Flow<T> replace(Collection<T> data) {
        original.clear();
        if (data != null) {
            original.addAll(data);
        }
        return this;
    }

    public Flow<T> remove(Filter<T> filter) {
        Iterator<T> iterator = original.iterator();
        while (iterator.hasNext()) {
            if (filter.filter(iterator.next())) {
                iterator.remove();
            }
        }
        return this;
    }

    public Flow<T> increase(Creator<T> creator) {
        original.addAll(creator.create());
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

    public <V> Flow<V> mapAll(Function<List<T>, V> function) {
        return Flow.with(function.apply(new ArrayList<>(original)));
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

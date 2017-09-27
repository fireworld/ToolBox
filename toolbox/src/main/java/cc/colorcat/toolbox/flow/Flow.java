package cc.colorcat.toolbox.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Flow<T> {
    public static <T> Flow<T> create() {
        return new Flow<>();
    }

    public static <T> Flow<T> from(Collection<? extends T> col) {
        return new Flow<>(col);
    }

    @SafeVarargs
    public static <T> Flow<T> from(T... ts) {
        return new Flow<>(Arrays.asList(ts));
    }

    public static <T> Flow<T> just(T value) {
        return new Flow<>(value);
    }

    private List<T> list;

    private Flow() {
        this.list = new ArrayList<>();
    }

    private Flow(T t) {
        this.list = new ArrayList<>(1);
        this.list.add(t);
    }

    private Flow(Collection<? extends T> col) {
        this.list = new ArrayList<>(col);
    }

    public Flow<T> add(T element) {
        list.add(element);
        return this;
    }

    public Flow<T> concat(Flow<? extends T> flow) {
        list.addAll(flow.list);
        return this;
    }

    public Flow<T> concat(Collection<? extends T> col) {
        list.addAll(col);
        return this;
    }

    public Flow<T> increase(Func0<Collection<? extends T>> func) {
        list.addAll(func.apply());
        return this;
    }

    public Flow<T> filter(Func1<? super T, Boolean> func) {
        final int size = list.size();
        List<T> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            T t = list.get(i);
            if (func.apply(t)) {
                newList.add(t);
            }
        }
        return Flow.from(newList);
    }

    public Flow<Integer> index(Func1<? super T, Boolean> func) {
        List<Integer> index = new ArrayList<>();
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (func.apply(list.get(i))) {
                index.add(i);
            }
        }
        return Flow.from(index);
    }

    public <R> Flow<R> map(Func1<? super T, ? extends R> func) {
        final int size = list.size();
        List<R> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            newList.add(func.apply(list.get(i)));
        }
        return Flow.from(newList);
    }

    public <R> Flow<R> flatMap(Func1<? super T, ? extends Flow<? extends R>> func) {
        Flow<R> newFlow = Flow.create();
        for (int i = 0, size = list.size(); i < size; ++i) {
            newFlow.concat(func.apply(list.get(i)));
        }
        return newFlow;
    }

    public <R> Flow<R> mapAll(Func1<List<T>, ? extends R> func) {
        return Flow.just(func.apply(new ArrayList<>(list)));
    }

    public Flow<Boolean> contains(final T element) {
        return Flow.just(list.contains(element));
    }

    public Flow<Boolean> exists(Func1<T, Boolean> func) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (func.apply(list.get(i))) {
                return Flow.just(Boolean.TRUE);
            }
        }
        return Flow.just(Boolean.FALSE);
    }

    public Flow<Boolean> all(Func1<T, Boolean> func) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (!func.apply(list.get(i))) {
                return Flow.just(Boolean.FALSE);
            }
        }
        return Flow.just(Boolean.TRUE);
    }

    public Flow<T> remove(final T element) {
        list.remove(element);
        return this;
    }

    public Flow<T> removeAll(Collection<? extends T> col) {
        list.removeAll(col);
        return this;
    }

    public Flow<T> clear() {
        list.clear();
        return this;
    }

    public Flow<T> remove(Func1<T, Boolean> func) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (func.apply(iterator.next())) {
                iterator.remove();
            }
        }
        return this;
    }

    public Flow<T> sort(Comparator<? super T> comparator) {
        Collections.sort(list, comparator);
        return this;
    }

    public Flow<T> reverse() {
        Collections.reverse(list);
        return this;
    }

    public Flow<T> take(int count) {
        int size = Math.min(count, list.size());
        List<T> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            newList.add(list.get(i));
        }
        return Flow.from(newList);
    }

    public Flow<T> takeEnd(int count) {
        int size = list.size();
        int realCount = Math.min(count, size);
        List<T> newList = new ArrayList<>(realCount);
        for (int i = size - realCount; i < size; ++i) {
            newList.add(list.get(i));
        }
        return Flow.from(newList);
    }

    public Flow<T> takeFirst(Func1<? super T, Boolean> func) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            T t = list.get(i);
            if (func.apply(t)) {
                return Flow.just(t);
            }
        }
        return Flow.create();
    }

    public Flow<T> takeLast(Func1<? super T, Boolean> func) {
        for (int i = list.size() - 1; i >= 0; --i) {
            T t = list.get(i);
            if (func.apply(t)) {
                return Flow.just(t);
            }
        }
        return Flow.create();
    }

    public Flow<T> doOnNext(Action1<? super T> action) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            action.call(list.get(i));
        }
        return this;
    }

    public Flow<T> forEach(Action1<T> action) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            action.call(list.get(i));
        }
        return this;
    }

    public Flow<T> collect(Action1<List<T>> action) {
        action.call(new ArrayList<>(list));
        return this;
    }

    public Flow<T> collectSkipEmpty(Action1<List<T>> action) {
        if (!list.isEmpty()) {
            action.call(new ArrayList<>(list));
        }
        return this;
    }

    public void complete(Action0 action) {
        action.call();
    }
}

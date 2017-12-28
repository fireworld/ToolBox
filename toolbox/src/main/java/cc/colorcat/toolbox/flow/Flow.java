package cc.colorcat.toolbox.flow;

import java.util.*;

@SuppressWarnings("unused")
public class Flow<T> {

    public static <T> Flow<T> operate(List<T> original) {
        if (original == null) {
            throw new NullPointerException("original == null");
        }
        return new Flow<>(original);
    }

    public static <T> Flow<T> create() {
        return new Flow<>(new ArrayList<T>());
    }

    public static <T> Flow<T> create(int capacity) {
        return new Flow<>(new ArrayList<T>(capacity));
    }

    public static <T> Flow<T> just(T value) {
        return Flow.from(Collections.singletonList(value));
    }

    @SafeVarargs
    public static <T> Flow<T> from(T... ts) {
        return Flow.from(Arrays.asList(ts));
    }

    public static <T> Flow<T> from(Collection<? extends T> ts) {
        return new Flow<>(new ArrayList<>(ts));
    }


    private List<T> original;

    private Flow(List<T> original) {
        this.original = original;
    }

    public Flow<T> raise(Func0<? extends Flow<? extends T>> func) {
        return concat(func.apply());
    }

    public Flow<T> concat(Flow<? extends T> flow) {
        original.addAll(flow.original);
        return this;
    }

    public Flow<T> concat(Collection<? extends T> ts) {
        original.addAll(ts);
        return this;
    }

    public <R, O> Flow<R> zip(Flow<? extends O> flow, Func2<? super T, ? super O, ? extends R> func) {
        final int size = Math.min(original.size(), flow.original.size());
        List<R> zipped = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            T t = this.original.get(i);
            O o = flow.original.get(i);
            R r = func.apply(t, o);
            zipped.add(r);
        }
        return Flow.operate(zipped);
    }

    public Flow<T> filter(Func1<? super T, Boolean> func) {
        final int size = original.size();
        List<T> filtered = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            T t = original.get(i);
            if (func.apply(t)) {
                filtered.add(t);
            }
        }
        return Flow.operate(filtered);
    }

    public <R> Flow<R> map(Func1<? super T, ? extends R> func) {
        final int size = original.size();
        List<R> mapped = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            mapped.add(func.apply(original.get(i)));
        }
        return Flow.operate(mapped);
    }

    public <R> Flow<R> flatMap(Func1<? super T, ? extends Flow<? extends R>> func) {
        final int size = original.size();
        Flow<R> newFlow = Flow.create(size);
        for (int i = 0; i < size; ++i) {
            newFlow.concat(func.apply(original.get(i)));
        }
        return newFlow;
    }

    public <R> Flow<R> mapAll(Func1<? super List<T>, ? extends R> func) {
        return Flow.just(func.apply(new ArrayList<>(original)));
    }

    public Flow<Integer> index(Func1<? super T, Boolean> func) {
        List<Integer> index = new ArrayList<>();
        for (int i = 0, size = original.size(); i < size; ++i) {
            if (func.apply(original.get(i))) {
                index.add(i);
            }
        }
        return Flow.operate(index);
    }

    public int indexOf(Func1<? super T, Boolean> func) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            if (func.apply(original.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public Flow<Boolean> exists(Func1<? super T, Boolean> func) {
        return Flow.just(indexOf(func) != -1);
    }

    public Flow<Boolean> allMatch(Func1<? super T, Boolean> func) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            if (!func.apply(original.get(i))) {
                return Flow.just(Boolean.FALSE);
            }
        }
        return Flow.just(Boolean.TRUE);
    }

    public Flow<T> remove(Func1<? super T, Boolean> func) {
        Iterator<T> iterator = original.iterator();
        while (iterator.hasNext()) {
            if (func.apply(iterator.next())) {
                iterator.remove();
            }
        }
        return this;
    }

    public Flow<T> take(int count) {
        final int size = Math.min(count, original.size());
        List<T> took = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            took.add(original.get(i));
        }
        return Flow.operate(took);
    }

    public Flow<T> takeEnd(int count) {
        final int size = original.size();
        final int realCount = Math.min(count, size);
        List<T> took = new ArrayList<>(realCount);
        for (int i = size - realCount; i < size; ++i) {
            took.add(original.get(i));
        }
        return Flow.operate(took);
    }

    public T find(Func1<? super T, Boolean> func) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            T t = original.get(i);
            if (func.apply(t)) {
                return t;
            }
        }
        return null;
    }

    public T findLast(Func1<? super T, Boolean> func) {
        for (int i = original.size() - 1; i >= 0; --i) {
            T t = original.get(i);
            if (func.apply(t)) {
                return t;
            }
        }
        return null;
    }

    public Flow<T> forEach(Action1<? super T> action) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            action.call(original.get(i));
        }
        return this;
    }

    public Flow<T> reverse() {
        Collections.reverse(original);
        return this;
    }

    public Flow<T> sort(Comparator<? super T> comparator) {
        Collections.sort(original, comparator);
        return this;
    }

    public Flow<T> callElse(Action0Else action0Else) {
        if (original.isEmpty()) {
            action0Else.empty();
        } else {
            action0Else.call();
        }
        return this;
    }

    public Flow<T> eachElse(Action1Else<? super T> action1Else) {
        final int size = original.size();
        if (size == 0) {
            action1Else.empty();
        } else {
            for (int i = 0; i < size; ++i) {
                action1Else.call(original.get(i));
            }
        }
        return this;
    }

    public Flow<T> collect(Action1<? super List<T>> action) {
        action.call(new ArrayList<>(original));
        return this;
    }

    public Flow<T> collectSkipEmpty(Action1<? super List<T>> action) {
        if (!original.isEmpty()) {
            action.call(new ArrayList<>(original));
        }
        return this;
    }

    public void complete(Action0 action) {
        action.call();
    }

    public List<T> original() {
        return original;
    }
}

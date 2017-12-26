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

    public static <T> Flow<T> merge(Collection<? extends T> c1, Collection<? extends T> c2) {
        List<T> ts = new ArrayList<>(c1.size() + c2.size());
        ts.addAll(c1);
        ts.addAll(c2);
        return Flow.operate(ts);
    }

    public static <T1, T2, R> Flow<? extends R> zip(Flow<? extends T1> f1, Flow<? extends T2> f2, Func2<? super T1, ? super T2, ? extends R> func) {
        return f1.zip(f2, func);
    }

    public static <T> Flow<T> just(T value) {
        return from(Collections.singletonList(value));
    }

    @SafeVarargs
    public static <T> Flow<T> from(T... ts) {
        return from(Arrays.asList(ts));
    }

    public static <T> Flow<T> from(Collection<? extends T> ts) {
        return new Flow<>(new ArrayList<>(ts));
    }


    private List<T> original;

    private Flow(List<T> original) {
        this.original = original;
    }

    public Flow<T> raise(Func0<? extends Flow<? extends T>> func) {
        return merge(func.apply());
    }

    public Flow<T> merge(Flow<? extends T> flow) {
        original.addAll(flow.original);
        return this;
    }

    public Flow<T> concat(Collection<? extends T> ts) {
        original.addAll(ts);
        return this;
    }

    public <R, O> Flow<R> zip(Flow<? extends O> flow, Func2<? super T, ? super O, ? extends R> func) {
        int size = Math.min(original.size(), flow.original.size());
        List<R> newList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            T t = original.get(i);
            O o = flow.original.get(i);
            R r = func.apply(t, o);
            newList.add(r);
        }
        return Flow.operate(newList);
    }

    public Flow<T> filter(Func1<? super T, Boolean> func) {
        final int size = original.size();
        List<T> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            T t = original.get(i);
            if (func.apply(t)) {
                newList.add(t);
            }
        }
        return Flow.operate(newList);
    }

    public <R> Flow<R> map(Func1<? super T, ? extends R> func) {
        final int size = original.size();
        List<R> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            newList.add(func.apply(original.get(i)));
        }
        return Flow.operate(newList);
    }

    public <R> Flow<R> flatMap(Func1<? super T, ? extends Flow<? extends R>> func) {
        Flow<R> newFlow = Flow.create();
        for (int i = 0, size = original.size(); i < size; ++i) {
            newFlow.merge(func.apply(original.get(i)));
        }
        return newFlow;
    }

    public <R> Flow<R> mapAll(Func1<List<T>, ? extends R> func) {
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

    public boolean allMatch(Func1<? super T, Boolean> func) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            if (!func.apply(original.get(i))) {
                return false;
            }
        }
        return true;
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
        int size = Math.min(count, original.size());
        List<T> newList = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            newList.add(original.get(i));
        }
        return Flow.operate(newList);
    }

    public Flow<T> takeEnd(int count) {
        int size = original.size();
        int realCount = Math.min(count, size);
        List<T> newList = new ArrayList<>(realCount);
        for (int i = size - realCount; i < size; ++i) {
            newList.add(original.get(i));
        }
        return Flow.operate(newList);
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

    public Flow<T> doOnNext(Action1<? super T> action) {
        for (int i = 0, size = original.size(); i < size; ++i) {
            action.call(original.get(i));
        }
        return this;
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

    public Flow<T> sorted(Comparator<? super T> comparator) {
        Collections.sort(original, comparator);
        return this;
    }

    public Flow<T> collect(Action1<List<T>> action) {
        action.call(new ArrayList<>(original));
        return this;
    }

    public Flow<T> collectSkipEmpty(Action1<List<T>> action) {
        if (!original.isEmpty()) {
            action.call(new ArrayList<>(original));
        }
        return this;
    }

    public void complete(Action0 action) {
        action.call();
    }

    public int size() {
        return original.size();
    }

    public List<T> original() {
        return original;
    }
}

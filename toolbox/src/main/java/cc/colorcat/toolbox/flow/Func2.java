package cc.colorcat.toolbox.flow;

/**
 * Created by cxx on 2017/12/19.
 * xx.ch@outlook.com
 */
public interface Func2<T1, T2, R> extends Function {
    R apply(T1 t1, T2 t2);
}

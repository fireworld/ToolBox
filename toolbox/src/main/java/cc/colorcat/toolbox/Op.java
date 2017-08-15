package cc.colorcat.toolbox;

import java.util.Collections;
import java.util.List;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public final class Op {

    public static <T> List<T> nullEmpty(List<T> list) {
        return list != null ? list : Collections.<T>emptyList();
    }

    public static <T> T nullElse(T value, T other) {
        return value != null ? value : other;
    }

    public static <T> T nonNull(T value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        return value;
    }

    public static <T> T nonNull(T value, String msg) {
        if (value == null) {
            throw new NullPointerException(msg);
        }
        return value;
    }

    public static <T, E extends Throwable> T nonNull(T value, E e) throws E {
        if (value == null) {
            throw e;
        }
        return value;
    }

    public static <T extends CharSequence> T emptyElse(T value, T other) {
        return value != null && value.length() != 0 ? value : other;
    }

    private Op() {
        throw new AssertionError("no instance");
    }
}

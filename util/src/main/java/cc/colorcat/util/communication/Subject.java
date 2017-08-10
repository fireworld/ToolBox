package cc.colorcat.util.communication;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public final class Subject {
    private String msg;
    private Object extra;

    private Subject(String msg, Object extra) {
        this.msg = msg;
        this.extra = extra;
    }

    public String msg() {
        return msg;
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T extra() {
        return (T) extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (!msg.equals(subject.msg)) return false;
        return extra != null ? extra.equals(subject.extra) : subject.extra == null;

    }

    @Override
    public int hashCode() {
        int result = msg.hashCode();
        result = 31 * result + (extra != null ? extra.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "msg='" + msg + '\'' +
                ", extra=" + extra +
                '}';
    }

    public static Subject create(String msg, Object extra) {
        if (msg == null) {
            throw new NullPointerException("msg == null");
        }
        if (extra == null) {
            throw new NullPointerException("extra == null");
        }
        return new Subject(msg, extra);
    }

    public static Subject create(String msg) {
        if (msg == null) {
            throw new NullPointerException("msg == null");
        }
        return new Subject(msg, null);
    }
}

package cc.colorcat.util.communication;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public final class Subject {
    private String action;
    private Object pack;

    private Subject(String action, Object pack) {
        this.action = action;
        this.pack = pack;
    }

    public String getAction() {
        return action;
    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getPack() {
        return (T) pack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (!action.equals(subject.action)) return false;
        return pack != null ? pack.equals(subject.pack) : subject.pack == null;
    }

    @Override
    public int hashCode() {
        int result = action.hashCode();
        result = 31 * result + (pack != null ? pack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "action='" + action + '\'' +
                ", pack=" + pack +
                '}';
    }

    public static Subject create(String action, Object pack) {
        if (action == null) {
            throw new NullPointerException("action == null");
        }
        if (pack == null) {
            throw new NullPointerException("pack == null");
        }
        return new Subject(action, pack);
    }

    public static Subject create(String action) {
        if (action == null) {
            throw new NullPointerException("action == null");
        }
        return new Subject(action, null);
    }
}

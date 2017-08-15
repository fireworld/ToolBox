package cc.colorcat.toolbox;

/**
 * Created by cxx on 17-8-15.
 * xx.ch@outlook.com
 */
public class Result<T> {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FRIENDLY = 1;
    public static final int CODE_SIGN_EXPIRED = 2;
    public static final int CODE_SYSTEM_ERROR = 3;

    public static final String MSG_SUCCESS = "ok";
    public static final String MSG_SIGN_EXPIRED = "sign expired";

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> onSuccess(T t) {
        if (t == null) {
            throw new IllegalArgumentException(" t == null");
        }
        return new Result<>(CODE_SUCCESS, MSG_SUCCESS, t);
    }

    public static <T> Result<T> onFriendly(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException("msg == null");
        }
        if (msg.equalsIgnoreCase(MSG_SUCCESS)) {
            throw new IllegalArgumentException("failure, but msg is " + MSG_SUCCESS);
        }
        return new Result<>(CODE_FRIENDLY, msg, null);
    }

    public static <T> Result<T> onExpired() {
        return new Result<>(CODE_SIGN_EXPIRED, MSG_SIGN_EXPIRED, null);
    }

    public static <T> Result<T> onSystemError(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException("msg == null");
        }
        if (msg.equalsIgnoreCase(MSG_SUCCESS)) {
            throw new IllegalArgumentException("failure, but msg is " + MSG_SUCCESS);
        }
        return new Result<>(CODE_SYSTEM_ERROR, msg, null);
    }


    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?> result = (Result<?>) o;

        if (code != result.code) return false;
        if (!msg.equals(result.msg)) return false;
        return data != null ? data.equals(result.data) : result.data == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + msg.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

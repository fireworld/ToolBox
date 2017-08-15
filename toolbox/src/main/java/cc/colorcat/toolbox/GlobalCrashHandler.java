package cc.colorcat.toolbox;

/**
 * Created by cxx on 2017/7/14.
 * xx.ch@outlook.com
 */
public class GlobalCrashHandler implements Thread.UncaughtExceptionHandler {
    private static volatile GlobalCrashHandler instance;

    private Thread.UncaughtExceptionHandler defaultHandler; // 系统默认的异常处理类
    private Listener mListener;

    private GlobalCrashHandler() {

    }

    public static GlobalCrashHandler getInstance() {
        if (instance == null) {
            synchronized (GlobalCrashHandler.class) {
                if (instance == null) {
                    instance = new GlobalCrashHandler();
                }
            }
        }
        return instance;
    }

    public void init() {
        this.init(null);
    }

    public void init(Listener listener) {
        mListener = listener;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable cause) {
        if (mListener != null && mListener.handle(thread, cause)) return;
        defaultHandler.uncaughtException(thread, cause);
    }

    public interface Listener {

        /**
         * 如是手动处理完异常返回 true，否则返回 false
         */
        boolean handle(Thread thread, Throwable cause);
    }
}

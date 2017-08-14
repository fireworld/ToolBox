package cc.colorcat.toolbox;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 功能说明：对 TextView 及其子类进行包装以显示倒计时计数, 并可设定在不同倒计时计数状态下的监听.
 * 注意：倒计时总时间为: 倒计时总计数 * 1000 / 倒计时间隔, 默认倒计时总计数为 60, 倒计时间隔为 1000 毫秒
 * <p/>
 * Created by cxx on 15/10/9.
 * xx.ch@outlook.com
 */
public class CountTimer<V extends TextView> {
    /**
     * 倒计时计数状态——已取消或已停止
     */
    public static final int STATE_CANCEL = -1;
    /**
     * 倒计时计数状态——已暂停
     */
    public static final int STATE_PAUSE = 0;
    /**
     * 倒计时计数状态——进行中
     */
    public static final int STATE_GOING = 1;

    private int mTotalCount = 60; // 倒计时总计数
    private int mCount = mTotalCount; // 当前倒计时计数

    private int mInterval = 1000; // 倒计时计数间隔时间, 以毫秒为单位
    @State
    private int mState = STATE_CANCEL; // 倒计时计数状态

    private V mView; // 显示倒计时计数的 View
    private CharSequence mViewTextBak; // 倒计时计数前 mView 显示的文字，用于倒计时结束后重置 mView 显示的文字

    private OnCountDownListener<V> mCountDownListener; // 倒计时计数的监听
    private OnStateChangeListener<V> mStateListener; // 倒计时计数状态的监听
    protected final Handler mHandler = new Handler(Looper.getMainLooper());

    private String mFormat; // 显示倒计时计数的文字格式，如想显示成 "还有 xx 秒"，则应设置为 "还有 %d 秒"

    /**
     * @param v 显示倒计时计数的 View, 不能为空
     */
    public CountTimer(@NonNull V v) {
        this(v, -1, -1);
    }

    /**
     * @param v          显示倒计时计数的 View, 不能为空
     * @param totalCount 倒计时总计数, 须大于 0
     */
    public CountTimer(@NonNull V v, int totalCount) {
        this(v, totalCount, -1);
    }

    /**
     * 倒计时总时间: totalCount * 1000 / intervalInMilliseconds
     *
     * @param v                      显示倒计时计数的 View, 不能为空
     * @param totalCount             倒计时总计数, 须大于 0
     * @param intervalInMilliseconds 倒计时计数间隔时间, 须大于 0, 以毫秒为单位
     */
    public CountTimer(@NonNull V v, int totalCount, int intervalInMilliseconds) {
        mView = checkNotNull(v, "v == null");
        if (totalCount > 0) {
            mTotalCount = totalCount;
        }
        if (intervalInMilliseconds > 0) {
            mInterval = intervalInMilliseconds;
        }
    }

    /**
     * @param listener 倒计时计数的监听
     */
    public void setOnCountDownListener(OnCountDownListener<V> listener) {
        mCountDownListener = listener;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    private void notifyCountDownStart() {
        if (mCountDownListener != null) {
            mCountDownListener.onStart(mView, mCount);
        }
    }

    private void notifyCountDownChanged() {
        if (mCountDownListener != null) {
            mCountDownListener.onCountDown(mView, mCount);
        }
    }

    private void notifyCountDownEnd() {
        if (mCountDownListener != null) {
            mCountDownListener.onEnd(mView, mCount);
        }
    }

    /**
     * @param listener 倒计时计数状态的监听
     */
    public void setOnStateChangeListener(OnStateChangeListener<V> listener) {
        mStateListener = listener;
    }

    private void notifyStateChanged() {
        if (mStateListener != null) {
            mStateListener.onStateChange(mView, mState);
        }
    }

    /**
     * @param totalCount 倒计时总计数, 须大于 0
     */
    public void setTotalCount(int totalCount) {
        if (totalCount > 0) {
            mTotalCount = totalCount;
        }
    }

    /**
     * @param intervalInMilliseconds 倒计时计数间隔时间, 须大于 0, 以毫秒为单位
     */
    public void setInterval(int intervalInMilliseconds) {
        if (intervalInMilliseconds > 0) {
            mInterval = intervalInMilliseconds;
        }
    }

    /**
     * @param v 显示倒计时计数的 View, 不能为空
     */
    public void setView(@NonNull V v) {
        mView = checkNotNull(v, "v == null");
    }

    /**
     * @return 显示倒计时计数的 View
     */
    public V getView() {
        return mView;
    }

    /**
     * 开始倒计时计数
     */
    public void start() {
        if (mState == STATE_CANCEL) {
            backupViewText();
            resetCount();
            notifyCountDownStart();
            execute();
        }
    }

    /**
     * 重新开始倒计时计数, 用于暂停后恢复倒计时计数
     */
    public void restart() {
        if (mState == STATE_PAUSE) {
            execute();
        }
    }

    /**
     * 备份显示倒计时计数的 View 的文字, 以便倒计时计数结束或取消后进行恢复
     */
    private void backupViewText() {
        mViewTextBak = mView.getText();
    }

    /**
     * 执行倒计时计数
     */
    private void execute() {
        mState = STATE_GOING;
        notifyStateChanged();
        mHandler.postDelayed(mRunnable, mInterval);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mState != STATE_GOING) return;
            mCount--;
            if (mCount > 0) {
                mView.setText(format(mCount));
                notifyCountDownChanged();
                mHandler.postDelayed(mRunnable, mInterval);
            } else {
                cancel();
                notifyCountDownEnd();
            }
        }
    };

    private String format(int count) {
        if (mFormat == null) {
            return Integer.toString(count);
        } else {
            return String.format(mFormat, count);
        }
    }

    /**
     * 取消倒计时计数
     */
    public void cancel() {
        if (mState != STATE_CANCEL) {
            mState = STATE_CANCEL;
            notifyStateChanged();
            resetViewText();
            resetCount();
        }
    }

    /**
     * 暂停倒计时计数
     */
    public void pause() {
        if (mState == STATE_GOING) {
            mState = STATE_PAUSE;
            notifyStateChanged();
        }
    }

    /**
     * 重置倒计时计数
     */
    private void resetCount() {
        mCount = mTotalCount;
    }

    /**
     * 重置显示倒计时计数的 View 显示的文字
     */
    private void resetViewText() {
        mView.setText(mViewTextBak);
    }

    /**
     * 倒计时计数监听接口
     *
     * @param <V> 显示倒计时计数的 View
     */
    public interface OnCountDownListener<V extends TextView> {
        /**
         * 倒计时计数开始时被调用，被暂停后继续之前的计数时不会被调用
         *
         * @param v            显示倒计时计数的 View
         * @param currentCount 当前倒计时计数
         */
        void onStart(@NonNull V v, int currentCount);

        /**
         * 倒计时计数进行中被调用
         *
         * @param v            显示倒计时计数的 View
         * @param currentCount 当前倒计时计数
         */
        void onCountDown(@NonNull V v, int currentCount);

        /**
         * 倒计时计数正常结束（即倒计时计数归零）时被调用，被 Cancel 掉很可能不会调用，除非此时刚好计数归零
         *
         * @param v            显示倒计时计数的 View
         * @param currentCount 当前倒计时计数
         */
        void onEnd(@NonNull V v, int currentCount);
    }

    /**
     * 倒计时计数状态监听接口
     *
     * @param <V> 显示倒计时计数的 View
     */
    public interface OnStateChangeListener<V extends TextView> {
        /**
         * 倒计时计数状态改变时被调用
         *
         * @param v     显示倒计时计数的 View
         * @param state 倒计时计数的状态, 可能为: CountTimer.STATE_CANCEL, CountTimer.STATE_PAUSE, CountTimer.STATE_GOING
         */
        void onStateChange(@NonNull V v, @State int state);
    }

    @IntDef({CountTimer.STATE_CANCEL, CountTimer.STATE_GOING, CountTimer.STATE_PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private static <T> T checkNotNull(T t, String msg) {
        if (t == null) {
            throw new NullPointerException(msg);
        }
        return t;
    }
}
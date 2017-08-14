package cc.colorcat.util;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * 功能说明: 简单的倒计时计数类, 在计时开始后自动将显示倒计时的 View 设定为不可用状态, 倒计时结束后恢复为可用状态.
 * 注意: 此类的 setOnStateChangeListener() 方法无效, 如果需要设定状态监听, 请使用 CountTimer 类.
 * Created by cxx on 15/10/15.
 */
public class SimpleCountTimer<V extends TextView> extends CountTimer<V> {

    {
        super.setOnStateChangeListener(new OnStateChangeListener<V>() {
            @Override
            public void onStateChange(@NonNull V v, int state) {
                final V view = v;
                final int currentState = state;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(currentState == CountTimer.STATE_CANCEL);
                    }
                });
            }
        });
    }

    public SimpleCountTimer(@NonNull V v) {
        super(v);
    }

    public SimpleCountTimer(@NonNull V v, int totalCount) {
        super(v, totalCount);
    }

    public SimpleCountTimer(@NonNull V v, int totalCount, int intervalInMilliseconds) {
        super(v, totalCount, intervalInMilliseconds);
    }

    /**
     * 请勿调用此方法，否则会抛异常, 如需设置状态监听, 请使用 CountTimer
     *
     * @param listener 倒计时计数状态的监听
     * @throws UnsupportedOperationException 调用此方法即抛出此异常
     * @deprecated 建议使用 CountTimer 以设置状态监听
     */
    @Override
    public void setOnStateChangeListener(OnStateChangeListener<V> listener) {
        throw new UnsupportedOperationException("SimpleCountTimer can not set OnStateChangeListener.");
    }
}

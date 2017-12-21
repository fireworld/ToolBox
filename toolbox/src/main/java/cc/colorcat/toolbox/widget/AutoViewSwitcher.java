package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewSwitcher;

/**
 * Created by cxx on 2017/12/18.
 * xx.ch@outlook.com
 */
public class AutoViewSwitcher extends ViewSwitcher {
    private int mInterval = 2000;
    private int mCurrent = 0;
    private boolean mLoopEnable = false;
    private ViewBinder mViewBinder;

    public AutoViewSwitcher(Context context) {
        super(context);
    }

    public AutoViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mLoopEnable = enabled;
        if (!mLoopEnable) {
            pauseLoop();
        }
    }

    @Override
    public void showNext() {
        updateView(true);
        super.showNext();
    }

    public void showCurrent() {
        ViewBinder binder = mViewBinder;
        if (binder != null && binder.getItemCount() > 0) {
            if (mCurrent < 0) {
                mCurrent = 0;
            } else {
                int last = binder.getItemCount() - 1;
                if (mCurrent > last) {
                    mCurrent = last;
                }
            }
            View view = getCurrentView();
            binder.bindView(view, mCurrent);
        }
    }

    @Override
    public void showPrevious() {
        updateView(false);
        super.showPrevious();
    }

    private Runnable mSwitcherRunnable;

    public void startLoop() {
        if (mViewBinder != null && mSwitcherRunnable == null) {
            mLoopEnable = true;
            mSwitcherRunnable = new ViewSwitcherRunnable();
            postDelayed(mSwitcherRunnable, mInterval);
        }
    }

    public void pauseLoop() {
        if (mSwitcherRunnable != null) {
            mLoopEnable = false;
            removeCallbacks(mSwitcherRunnable);
            mSwitcherRunnable = null;
        }
    }

    public void setViewBinder(ViewBinder binder) {
        mViewBinder = binder;
    }

    public void setSwitchInterval(int interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("interval <= " + 0);
        }
        mInterval = interval;
    }

    private void updateView(boolean next) {
        ViewBinder binder = mViewBinder;
        if (binder != null && binder.getItemCount() > 0) {
            if (next) {
                mCurrent++;
                if (mCurrent >= binder.getItemCount()) {
                    mCurrent = 0;
                }
            } else {
                mCurrent--;
                if (mCurrent < 0) {
                    mCurrent = binder.getItemCount() - 1;
                }
            }
            View view = getNextView();
            binder.bindView(view, mCurrent);
        }
    }

    private class ViewSwitcherRunnable implements Runnable {

        @Override
        public void run() {
            if (mLoopEnable) {
                showNext();
                postDelayed(this, mInterval);
            }
        }
    }


    public interface ViewBinder {

        int getItemCount();

        void bindView(View view, int position);
    }
}

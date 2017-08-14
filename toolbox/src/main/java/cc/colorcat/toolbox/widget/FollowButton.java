package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
 * Created by cxx on 16/9/20.
 * xx.ch@outlook.com
 */
public class FollowButton extends android.support.v7.widget.AppCompatButton {
    private static final int DEFAULT_STATUS_BAR = 24;
    private static final int THRESHOLD = 2;

    private int mDownX, mDownY;
    private int mLastX, mLastY;
    private int mOffsetW, mOffsetH;

    public FollowButton(Context context) {
        super(context);
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int w = right - left;
            int h = bottom - top;
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int screenW = dm.widthPixels;
            int screenH = dm.heightPixels;
            int statusBarH = getStatusBarHeight();
            mOffsetW = screenW - w;
            mOffsetH = screenH - h - statusBarH;
        }
    }

    private int getStatusBarHeight() {
        int result;
        Resources res = getResources();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = res.getDimensionPixelSize(resId);
        } else {
            result = (int) (res.getDisplayMetrics().density * DEFAULT_STATUS_BAR);
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = rawX;
                mDownY = rawY;
                mLastX = rawX;
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                final int x = (int) getX();
                final int y = (int) getY();
                if (rawX > mLastX) {
                    rawX = mLastX + Math.min(rawX - mLastX, mOffsetW - x);
                } else {
                    rawX = mLastX - Math.min(mLastX - rawX, x);
                }
                if (rawY > mLastY) {
                    rawY = mLastY + Math.min(rawY - mLastY, mOffsetH - y);
                } else {
                    rawY = mLastY - Math.min(mLastY - rawY, y);
                }
                setX(x + (rawX - mLastX));
                setY(y + (rawY - mLastY));
                mLastX = rawX;
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                return isIntercept(rawX, rawY, mDownX, mDownY) || super.onTouchEvent(event);
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private static boolean isIntercept(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) > THRESHOLD;
    }
}

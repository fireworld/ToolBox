package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cxx on 2016/9/20.
 * xx.ch@outlook.com
 */

public class FreeFollowButton extends android.support.v7.widget.AppCompatButton {
    private static final int THRESHOLD = 2;

    private float mDownX, mDownY;
    private float mLastX, mLastY;


    public FreeFollowButton(Context context) {
        super(context);
    }

    public FreeFollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreeFollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = rawX;
                mDownY = rawY;
                mLastX = rawX;
                mLastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                setX(getX() + (rawX - mLastX));
                setY(getY() + (rawY - mLastY));
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

    private static boolean isIntercept(float x1, float y1, float x2, float y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) > THRESHOLD;
    }
}

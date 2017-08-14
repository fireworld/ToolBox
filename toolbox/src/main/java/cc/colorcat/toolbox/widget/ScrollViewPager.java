package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 2016/9/14.
 * xx.ch@outlook.com
 */
public class ScrollViewPager extends ViewPager {

    private boolean mScrollEnabled = true;
    private boolean mInterceptEnabled = false;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewPager);
        mScrollEnabled = ta.getBoolean(R.styleable.ScrollViewPager_enable_scroll, true);
        mInterceptEnabled = ta.getBoolean(R.styleable.ScrollViewPager_enable_intercept, false);
        ta.recycle();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mInterceptEnabled) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return mScrollEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mScrollEnabled && super.onInterceptTouchEvent(event);
    }

    public void setScrollEnabled(boolean enabled) {
        mScrollEnabled = enabled;
    }

    public boolean isScrollEnabled() {
        return mScrollEnabled;
    }

    public void setInterceptEnabled(boolean enabled) {
        mInterceptEnabled = enabled;
    }

    public boolean isInterceptEnabled() {
        return mInterceptEnabled;
    }
}

package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 2016/10/11.
 * xx.ch@outlook.com
 */

public class InterceptSwipeRefreshLayout extends SwipeRefreshLayout {
    private CanChildScrollUpIntercept mIntercept;

    public InterceptSwipeRefreshLayout(Context context) {
        super(context);
    }

    public InterceptSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    public void setCanChildScrollUpIntercept(CanChildScrollUpIntercept intercept) {
        mIntercept = intercept;
    }

    @Override
    public boolean canChildScrollUp() {
        return (mIntercept != null && mIntercept.canScrollUp()) || super.canChildScrollUp();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InterceptSwipeRefreshLayout);
        boolean refreshEnabled = ta.getBoolean(R.styleable.InterceptSwipeRefreshLayout_refreshEnabled, true);
        setEnabled(refreshEnabled);
        ta.recycle();
    }

    public interface CanChildScrollUpIntercept {

        boolean canScrollUp();
    }
}

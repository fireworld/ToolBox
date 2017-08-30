package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cc.colorcat.toolbox.L;
import cc.colorcat.toolbox.R;
import cc.colorcat.toolbox.Utils;

/**
 * Created by cxx on 2017/8/30.
 * xx.ch@outlook.com
 */
public class TernaryTextView extends View {
    private Drawable[] mDrawables = new Drawable[3];
    private Rect[] mBounds = {new Rect(), new Rect(), new Rect()};
    private CharSequence mText;
    private int mTextSize;
    @ColorInt
    private int mTextColor;
    private Paint mTextPaint;

    public TernaryTextView(Context context) {
        super(context);
    }

    public TernaryTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    public TernaryTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TernaryTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        int defaultSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14F, context.getResources().getDisplayMetrics());
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TernaryTextView);
        mDrawables[0] = ta.getDrawable(R.styleable.TernaryTextView_drawableStart);
        mDrawables[1] = ta.getDrawable(R.styleable.TernaryTextView_drawableCenter);
        mDrawables[2] = ta.getDrawable(R.styleable.TernaryTextView_drawableEnd);
        mText = ta.getText(R.styleable.TernaryTextView_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TernaryTextView_textSize, defaultSize);
        mTextColor = ta.getColor(R.styleable.TernaryTextView_textColor, Color.DKGRAY);
        ta.recycle();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        L.e("onDraw");
        for (int size = mDrawables.length, index = 0; index < size; ++index) {
            Drawable drawable = mDrawables[index];
            if (drawable != null) {
                drawable.setBounds(mBounds[index]);
                drawable.draw(canvas);
            }
        }
        PointF point = computeTextStart();
        canvas.drawText(mText, 0, mText.length(), point.x, point.y, mTextPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        L.i(Utils.format("changed = %b, left = %d, top = %d, right = %d, bottom = %d",
                changed, left, top, right, bottom));
        if (changed) {
            int width = right - left, height = bottom - top;
            computeBounds(0, width, height);
            computeBounds(2, width, height);
            computeBounds(1, width, height);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        L.d("onMeasure");
    }

    private void computeBounds(int index, int viewWidth, int viewHeight) {
        Drawable drawable = mDrawables[index];
        mBounds[index].top = 0;
        mBounds[index].bottom = viewHeight;
        if (drawable != null) {
            if (index != 1) {
                int height = drawable.getMinimumHeight(), width = drawable.getMinimumWidth();
                int drawWidth = height != 0 ? viewHeight * width / height : 0;
                if (index == 0) {
                    mBounds[0].left = 0;
                    mBounds[0].right = mBounds[0].left + drawWidth;
                } else if (index == 2) {
                    mBounds[2].right = viewWidth;
                    mBounds[2].left = mBounds[2].right - drawWidth;
                }
            } else {
                mBounds[1].left = mBounds[0].right;
                mBounds[1].right = mBounds[2].left;
            }
        }
    }

    private int computeMinWidth() {
        int center = mDrawables[1] != null ? mDrawables[1].getMinimumWidth() : 0;
        center = (int) Math.max(center, mTextPaint.measureText(mText, 0, mText.length()));
        return mBounds[0].width() + mBounds[2].width() + center;
    }

    private int computeMinHeight() {
        int height = 0;
        for (int size = mDrawables.length, index = 0; index < size; index++) {
            Drawable drawable = mDrawables[index];
            if (drawable != null) {
                int tempHeight = drawable.getMinimumHeight();
                if (tempHeight > height) {
                    height = tempHeight;
                }
            }
        }
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        return Math.max(height, fontMetricsInt.bottom - fontMetricsInt.top);
    }

    private void fixBounds() {
        final int viewWidth = getWidth();
        if (mDrawables[0].getMinimumWidth() == 0
                && mDrawables[2].getMinimumWidth() == 0
                && mDrawables[1].getMinimumWidth() == 0) {
            int width = viewWidth / 3;
            mBounds[0].left = 0;
            mBounds[0].right = mBounds[0].left + width;
            mBounds[2].right = viewWidth;
            mBounds[2].left = mBounds[2].right - width;
            mBounds[1].left = mBounds[0].right;
            mBounds[1].right = mBounds[2].left;
        }
    }

    private PointF computeTextStart() {
        float width = mTextPaint.measureText(mText, 0, mText.length());
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        return new PointF((getWidth() - width) / 2, y);
    }

}

package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 2016/9/9.
 * xx.ch@outlook.com
 */
public class TranslateView extends View {
    private Bitmap mBitmap;
    private int mBitmapW;
    private boolean mScaled = false;
    private int mResId = 0;
    private float mSpeed;
    private Rect mClipBounds = new Rect();
    private float mOffset = 0;
    private boolean mTranslating;


    public TranslateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslateView(Context context, AttributeSet attrs, int styleID) {
        super(context, attrs, styleID);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TranslateView);
        mSpeed = ta.getDimension(R.styleable.TranslateView_speed, 10f);
        mResId = ta.getResourceId(R.styleable.TranslateView_src, -1);
        mTranslating = ta.getBoolean(R.styleable.TranslateView_translating, true);
        ta.recycle();
        if (mResId != -1) {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), mResId);
        }
        if (mBitmap == null) {
            throw new IllegalArgumentException("There is no legal src value in xml.");
        } else {
            mBitmapW = mBitmap.getWidth();
            addOnLayoutChangeListener(mListener);
        }
        if (mTranslating) {
            start();
        }
    }

    private OnLayoutChangeListener mListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (!mScaled) {
                Bitmap temp = mBitmap;
                mBitmap = Bitmap.createScaledBitmap(temp, right - left, bottom - top, true);
                mBitmapW = mBitmap.getWidth();
                temp.recycle();
                removeOnLayoutChangeListener(mListener);
                mScaled = true;
            }
        }
    };

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap.isRecycled()) {
            return;
        }

        canvas.getClipBounds(mClipBounds);
        while (mOffset <= -mBitmapW) {
            mOffset += mBitmapW;
        }
        float left = mOffset;
        while (left < mClipBounds.width()) {
            float drawLeft = calculateDrawLeft(mBitmapW, left);
            canvas.drawBitmap(mBitmap, drawLeft, 0, null);
            left += mBitmapW;
        }

        if (mTranslating && mSpeed != 0) {
            mOffset -= Math.abs(mSpeed);
            postInvalidateOnAnimation();
        }
    }

    private float calculateDrawLeft(float bitmapWidth, float left) {
        return mSpeed > 0 ? left : mClipBounds.width() - bitmapWidth - left;
    }

    public void start() {
        if (!mTranslating) {
            mTranslating = true;
            postInvalidateOnAnimation();
        }
    }

    public void stop() {
        if (mTranslating) {
            mTranslating = false;
            invalidate();
        }
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        if (mTranslating) {
            postInvalidateOnAnimation();
        }
    }
}

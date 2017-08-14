package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 16-3-22.
 * xx.ch@outlook.com
 */
public final class GeminiButton extends LinearLayout {
    private float mFontSize = 16;
    private int mMin = Integer.MIN_VALUE;
    private int mMax = Integer.MAX_VALUE;
    private int mInterval = 1;
    private int mNumber = 0;

    private boolean mInitialized = false;

    private int mOrientation = HORIZONTAL;
    private ViewGroup.LayoutParams mOuterLP; // GeminiButton 的布局参数
    private LayoutParams mInnerLP; // 内部 View 的布局参数
    private Button mMinusBTN;
    private TextView mNumberTV;
    private Button mPlusBTN;

    private OnNumberChangeListener mNumListener;

    public GeminiButton(Context context) {
        super(context);
        init();
    }

    public GeminiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
        init();
    }

    public GeminiButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttrs(context, attrs);
        init();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GeminiButton);
        mMin = ta.getInt(R.styleable.GeminiButton_min_number, Integer.MIN_VALUE);
        mMax = ta.getInt(R.styleable.GeminiButton_max_number, Integer.MAX_VALUE);
        mNumber = ta.getInt(R.styleable.GeminiButton_default_number, 0);
        mInterval = ta.getInt(R.styleable.GeminiButton_interval, 1);
        mFontSize = ta.getDimensionPixelSize(R.styleable.GeminiButton_text_size, 16);
        ta.recycle();
        if (mNumber < mMin || mNumber > mMax) {
            throw new IllegalArgumentException("min_number <= default_number <= max_number");
        }
        if (mInterval < 1) {
            throw new IllegalArgumentException("interval >= 1");
        }
    }

    private void init() {
        createInnerView();
        loadInnerView();
        mInitialized = true;
    }

    private void createInnerView() {
        mOrientation = getOrientation();
        if (mOrientation == HORIZONTAL) {
            mInnerLP = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        } else {
            mInnerLP = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        }
        mMinusBTN = createButton("-", mInnerLP);
        mPlusBTN = createButton("+", mInnerLP);
        mNumberTV = createTextView(String.valueOf(mNumber), mInnerLP);
    }

    private void reloadInnerView() {
        removeAllViews();
        loadInnerView();
    }

    private void loadInnerView() {
        if (mOrientation == HORIZONTAL) {
            addView(mMinusBTN);
            addView(mNumberTV);
            addView(mPlusBTN);
        } else {
            addView(mPlusBTN);
            addView(mNumberTV);
            addView(mMinusBTN);
        }
    }

    private void resetOuterLP() {
        if (mOuterLP != null) { // 可以删掉，此处仅是以防万一。
            int width = mOuterLP.width;
            int height = mOuterLP.height;
            int side = width < height ? width : height;
            if (mOrientation == HORIZONTAL) {
                width = side * 3;
                height = side;
            } else {
                height = side * 3;
                width = side;
            }
            mOuterLP.width = width;
            mOuterLP.height = height;
        }
    }

    private void resetInnerLP() {
        LayoutParams lp = mInnerLP;
        if (mOrientation == HORIZONTAL) {
            lp.height = LayoutParams.MATCH_PARENT;
            lp.width = 0;
        } else {
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = 0;
        }
    }

    @Override
    public void setOrientation(@OrientationMode int orientation) {
        super.setOrientation(orientation);
        if (mInitialized && mOrientation != orientation) {
            mOrientation = orientation;
            resetOuterLP();
            resetInnerLP();
            reloadInnerView();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mOuterLP == null) {
            mOuterLP = getLayoutParams();
            resetOuterLP();
        }
    }

    private Button createButton(CharSequence text, LayoutParams layoutParams) {
        Button button = new Button(getContext());
        button.setLayoutParams(layoutParams);
        button.setText(text);
        button.setTextSize(mFontSize);
        button.setOnClickListener(mClickListener);
        return button;
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mPlusBTN) {
                if (mNumber <= mMax - mInterval) {
                    mNumber += mInterval;
                } else {
                    mNumber = mMax;
                }
            } else if (v == mMinusBTN) {
                if (mNumber >= mMin + mInterval) {
                    mNumber -= mInterval;
                } else {
                    mNumber = mMin;
                }
            }
            notifyNumberChanged();
        }
    };

    private TextView createTextView(CharSequence text, LayoutParams layoutParams) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(mFontSize);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * @param interval 点击一次数字增加或减少的量, 须大于 0
     */
    public void setInterval(int interval) {
        if (interval > 0) {
            mInterval = interval;
        }
    }

    public void setNumberSize(float sizeInSP) {
        mNumberTV.setTextSize(sizeInSP);
    }

    /**
     * @param current 须不小于最小值, 不大于最大值
     */
    public void setNumber(int current, int min, int max) {
        if (current < min || current > max) {
            throw new IllegalArgumentException("min <= current <= max");
        }
        mMin = min;
        mMax = max;
        if (mNumber != current) {
            mNumber = current;
            notifyNumberChanged();
        }
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumberColor(@ColorInt int color) {
        mNumberTV.setTextColor(color);
    }

    public void setNumberColor(ColorStateList colors) {
        mNumberTV.setTextColor(colors);
    }

    public void setButtonTextSize(float sizeInSP) {
        mMinusBTN.setTextSize(sizeInSP);
        mPlusBTN.setTextSize(sizeInSP);
    }

    public void setButtonTextColor(@ColorInt int color) {
        mMinusBTN.setTextColor(color);
        mPlusBTN.setTextColor(color);
    }

    public void setButtonTextColor(ColorStateList colors) {
        mMinusBTN.setTextColor(colors);
        mPlusBTN.setTextColor(colors);
    }

    public void setMinusText(CharSequence text) {
        mMinusBTN.setText(text);
    }

    public void setMinusBackground(@DrawableRes int resId) {
        mMinusBTN.setBackgroundResource(resId);
    }

    public void setMinusBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mMinusBTN.setBackground(drawable);
        } else {
            mMinusBTN.setBackgroundDrawable(drawable);
        }
    }

    public void setMinusBackgroundColor(@ColorInt int color) {
        mMinusBTN.setBackgroundColor(color);
    }

    public void setPlusText(CharSequence text) {
        mPlusBTN.setText(text);
    }

    public void setPlusBackground(@DrawableRes int resId) {
        mPlusBTN.setBackgroundResource(resId);
    }

    public void setPlusBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mPlusBTN.setBackground(drawable);
        } else {
            mPlusBTN.setBackgroundDrawable(drawable);
        }
    }

    public void setPlusBackgroundColor(@ColorInt int color) {
        mPlusBTN.setBackgroundColor(color);
    }

    public void setOnNumberChangedListener(OnNumberChangeListener listener) {
        mNumListener = listener;
    }

    private void notifyNumberChanged() {
        mNumberTV.setText(String.valueOf(mNumber));
        if (mNumListener != null) {
            mNumListener.onNumberChanged(mNumber);
        }
    }

    @IntDef({LinearLayout.HORIZONTAL, LinearLayout.VERTICAL})
    @Retention(RetentionPolicy.CLASS)
    public @interface OrientationMode {
    }

    /**
     * 数字发生变化时的监听接口
     */
    public interface OnNumberChangeListener {
        /**
         * @param number 当前的数字
         */
        void onNumberChanged(int number);
    }
}
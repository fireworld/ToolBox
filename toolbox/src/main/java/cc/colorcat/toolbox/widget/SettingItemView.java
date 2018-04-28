package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 2018/4/24.
 * xx.ch@outlook.com
 */
public class SettingItemView extends LinearLayout {
    private TextView mLabel;
    private TextView mContent;
    private ImageView mIcon1;
    private ImageView mIcon2;

    public SettingItemView(Context context) {
        super(context);
        init(context, null);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        super.setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        int layout = ta.getResourceId(R.styleable.SettingItemView_layout, R.layout.setting_item);
        LayoutInflater.from(context).inflate(layout, this, true);
        mLabel = findViewById(android.R.id.text1);
        mLabel.setText(ta.getText(R.styleable.SettingItemView_label));
        mContent = findViewById(android.R.id.content);
        mContent.setText(ta.getText(R.styleable.SettingItemView_content));
        mIcon1 = findViewById(android.R.id.icon1);
        Drawable icon1 = ta.getDrawable(R.styleable.SettingItemView_icon1);
        mIcon2 = findViewById(android.R.id.icon2);
        Drawable icon2 = ta.getDrawable(R.styleable.SettingItemView_icon2);
        ta.recycle();

        if (icon1 != null) {
            mIcon1.setVisibility(View.VISIBLE);
            mIcon1.setImageDrawable(icon1);
        } else {
            mIcon1.setVisibility(View.GONE);
        }
        if (icon2 != null) {
            mIcon2.setVisibility(View.VISIBLE);
            mIcon2.setImageDrawable(icon2);
        } else {
            mIcon2.setVisibility(View.GONE);
        }
    }

    @Override
    public final void setOrientation(int orientation) {
    }

    public TextView getLabelTextView() {
        return mLabel;
    }

    public void setLabel(CharSequence text) {
        mLabel.setText(text);
    }

    public void setLabel(@StringRes int resId) {
        mLabel.setText(resId);
    }

    public CharSequence getLabel() {
        return mLabel.getText();
    }

    public TextView getContentTextView() {
        return mContent;
    }

    public void setContent(CharSequence content) {
        mContent.setText(content);
    }

    public void setContent(@StringRes int resId) {
        mContent.setText(resId);
    }

    public CharSequence getContent() {
        return mContent.getText();
    }

    public ImageView getIcon1ImageView() {
        return mIcon1;
    }

    public void setIcon1(Drawable drawable) {
        mIcon1.setImageDrawable(drawable);
    }

    public void setIcon1(Bitmap bitmap) {
        mIcon1.setImageBitmap(bitmap);
    }

    public void setIcon1(@DrawableRes int resId) {
        mIcon1.setImageResource(resId);
    }

    public ImageView getIcon2ImageView() {
        return mIcon2;
    }

    public void setIcon2(Drawable drawable) {
        mIcon2.setImageDrawable(drawable);
    }

    public void setIcon2(Bitmap bitmap) {
        mIcon2.setImageBitmap(bitmap);
    }

    public void setIcon2(@DrawableRes int resId) {
        mIcon2.setImageResource(resId);
    }
}

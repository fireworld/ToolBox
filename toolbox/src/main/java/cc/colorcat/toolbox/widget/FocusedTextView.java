package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by cxx on 16-6-27.
 * xx.ch@outlook.com
 */
public class FocusedTextView extends android.support.v7.widget.AppCompatTextView {
    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

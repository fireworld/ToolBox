package cc.colorcat.toolbox.widget;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;

/**
 * Created by cxx on 17-10-27.
 * xx.ch@outlook.com
 */
public class AutoCompleteEditText extends android.support.v7.widget.AppCompatEditText {
    private ListPopupWindow mPopupWindow;

    public AutoCompleteEditText(Context context) {
        super(context);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        mPopupWindow = new ListPopupWindow(context, attrs);
//        mPopupWindow.setSoftInputMode();
    }
}

package cc.colorcat.toolbox.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.ResultReceiver;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;

import cc.colorcat.toolbox.R;

/**
 * Created by cxx on 17-10-25.
 * xx.ch@outlook.com
 */
public class SearchBar extends FrameLayout {
    public static final String TAG = "SearchBar";

    private static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();

    private ImageView mSearchButton;
    private AutoCompleteTextView mSearchText;
    private ImageView mClearButton;

    private CharSequence mOldQuery;

    private boolean mClearingFocus;

    private OnFocusChangeListener mQueryFocusChangeListener;
    private OnQueryTextListener mQueryChangeListener;
    private OnSuggestionListener mSuggestionListener;
    private OnClickListener mSearchClickListener;

    private SuggestionsAdapter mAdapter;


    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchBar);
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_search_bar, this, true);

        mSearchButton = (ImageView) findViewById(R.id.search_button);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.search_text);
        mClearButton = (ImageView) findViewById(R.id.clear_button);

        mSearchButton.setImageDrawable(a.getDrawable(R.styleable.SearchBar_searchIcon));
        mClearButton.setImageDrawable(a.getDrawable(R.styleable.SearchBar_clearIcon));

        updateSearchButtonVisibility();
        updateClearButtonVisibility();

        mSearchButton.setOnClickListener(mClickListener);
        mClearButton.setOnClickListener(mClickListener);
        mSearchText.setOnClickListener(mClickListener);

        mSearchText.addTextChangedListener(mTextWatcher);
        mSearchText.setOnEditorActionListener(mEditorActionListener);
        mSearchText.setOnItemClickListener(mItemClickListener);
        mSearchText.setOnItemSelectedListener(mItemSelectedListener);
        mSearchText.setOnKeyListener(mKeyListener);
        mSearchText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setImeVisibility(hasFocus);
                if (mQueryFocusChangeListener != null) {
                    mQueryFocusChangeListener.onFocusChange(mSearchText, hasFocus);
                }
            }
        });

        CharSequence queryHint = a.getText(R.styleable.SearchBar_queryHint);
        if (!TextUtils.isEmpty(queryHint)) {
            mSearchText.setHint(queryHint);
        }
        a.recycle();
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return !mClearingFocus && isFocusable() && mSearchText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        super.clearFocus();
        mSearchText.clearFocus();
        setImeVisibility(false);
        mClearingFocus = false;
    }

    public void setOnQueryFocusChangeListener(OnFocusChangeListener listener) {
        mQueryFocusChangeListener = listener;
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mQueryChangeListener = listener;
    }

    public void setOnSuggestionListener(OnSuggestionListener listener) {
        mSuggestionListener = listener;
    }

    public void setOnSearchClickListener(OnClickListener listener) {
        mSearchClickListener = listener;
    }

    public void showDropDown() {
        mSearchText.showDropDown();
    }

    public void dismissDropDown() {
        mSearchText.dismissDropDown();
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mSearchButton) {
                onSearchClicked();
            } else if (v == mClearButton) {
                onClearClicked();
            }
        }
    };

    private void onSearchClicked() {
        if (mSearchText.requestFocus()) {
            setImeVisibility(true);
        }
        if (mSearchClickListener != null) {
            mSearchClickListener.onClick(mSearchButton);
        }
    }

    private void onClearClicked() {
        CharSequence text = mSearchText.getText();
        if (!TextUtils.isEmpty(text)) {
            mSearchText.setText("");
            mSearchText.requestFocus();
            setImeVisibility(true);
        }
    }

    private void forceSuggestionQuery() {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(mSearchText);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(mSearchText);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SearchBar.this.onTextChanged(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void onTextChanged(CharSequence newText) {
        updateClearButtonVisibility();
        if (mQueryChangeListener != null) {
            mQueryChangeListener.onQueryTextChange(newText.toString());
        }
    }

    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            onSubmitQuery();
            return true;
        }
    };

    private void onSubmitQuery() {
        CharSequence query = mSearchText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mQueryChangeListener == null
                    || !mQueryChangeListener.onQueryTextSubmit(query.toString())) {
                setImeVisibility(false);
                dismissSuggestions();
            }
        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onItemClicked(position, KeyEvent.KEYCODE_UNKNOWN, null);
        }
    };

    private boolean onItemClicked(int position, int actionKey, String actionMsg) {
        if (mSuggestionListener == null || !mSuggestionListener.onSuggestionClick(position)) {
            setImeVisibility(false);
            dismissSuggestions();
            return true;
        }
        return false;
    }

    private AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SearchBar.this.onItemSelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private OnKeyListener mKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (mSearchText.isPopupShowing()
                    && mSearchText.getListSelection() != ListView.INVALID_POSITION) {
                return onSuggestionsKey(v, keyCode, event);
            }

            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if (!queryTextEmpty() && event.hasNoModifiers()) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();

                        // Launch as a regular search.
//                        launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null, mSearchText.getText().toString());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    private boolean onItemSelected(int position) {
        if (mSuggestionListener == null
                || !mSuggestionListener.onSuggestionSelect(position)) {
//            rewriteQueryFromSuggestion(position);
            return true;
        }
        return false;
    }

    private boolean onSuggestionsKey(View v, int keyCode, KeyEvent event) {
        if (mAdapter == null) return false;
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.hasNoModifiers()) {
            // First, check for enter or search (both of which we'll treat as a "click")
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH
                    || keyCode == KeyEvent.KEYCODE_TAB) {
                int position = mSearchText.getListSelection();
                return onItemClicked(position, KeyEvent.KEYCODE_UNKNOWN, null);
            }

            // Next, check for left/right moves, which we use to "return" the
            // user to the edit view
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                // give "focus" to text editor, with cursor at the beginning if
                // left key, at end if right key
                // Arabic
                int selPoint = (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) ? 0 : mSearchText
                        .length();
                mSearchText.setSelection(selPoint);
                mSearchText.setListSelection(0);
                mSearchText.clearListSelection();
                HIDDEN_METHOD_INVOKER.ensureImeVisible(mSearchText, true);
                return true;
            }

            // Next, check for an "up and out" move
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP && 0 == mSearchText.getListSelection()) {
                // TODO: restoreUserQuery();
                // let ACTV complete the move
                return false;
            }
        }
        return false;
    }

    private boolean queryTextEmpty() {
        return TextUtils.getTrimmedLength(mSearchText.getText()) == 0;
    }

    private void updateSearchButtonVisibility() {
        final Drawable icon = mSearchButton.getDrawable();
        mSearchButton.setVisibility(icon != null ? VISIBLE : GONE);
    }

    private void updateClearButtonVisibility() {
        final boolean show = !TextUtils.isEmpty(mSearchText.getText());
        final Drawable icon = mClearButton.getDrawable();
        mClearButton.setVisibility(show && icon != null ? VISIBLE : GONE);
    }

    private void setImeVisibility(final boolean visible) {
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            if (visible) {
                imm.showSoftInput(mSearchText, InputMethodManager.SHOW_IMPLICIT);
            } else {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

//    private void setImeVisibility(final boolean visible) {
//        if (visible) {
//            post(mShowImeRunnable);
//        } else {
//            removeCallbacks(mShowImeRunnable);
//            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null) {
//                imm.hideSoftInputFromWindow(getWindowToken(), 0);
//            }
//        }
//    }

    private Runnable mShowImeRunnable = new Runnable() {
        @Override
        public void run() {
            InputMethodManager imm = getInputMethodManager();
            if (imm != null) {

                HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, SearchBar.this, 0);
            }
        }
    };

    private void dismissSuggestions() {
        mSearchText.dismissDropDown();
    }

    public void setSuggestionsAdapter(SuggestionsAdapter adapter) {
        if (mAdapter != adapter) {
            mAdapter = adapter;
            mSearchText.setAdapter(mAdapter);
        }
    }

    public void setThreshold(int threshold) {
        mSearchText.setThreshold(threshold);
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        boolean onQueryTextSubmit(String query);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }


    public interface OnSuggestionListener {

        /**
         * Called when a suggestion was selected by navigating to it.
         *
         * @param position the absolute position in the list of suggestions.
         * @return true if the listener handles the event and wants to override the default
         * behavior of possibly rewriting the query based on the selected item, false otherwise.
         */
        boolean onSuggestionSelect(int position);

        /**
         * Called when a suggestion was clicked.
         *
         * @param position the absolute position of the clicked item in the list of suggestions.
         * @return true if the listener handles the event and wants to override the default
         * behavior of launching any intent or submitting a search query specified on that item.
         * Return false otherwise.
         */
        boolean onSuggestionClick(int position);
    }


    private static class AutoCompleteTextViewReflector {
        private Method doBeforeTextChanged, doAfterTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        @SuppressLint("PrivateApi")
        AutoCompleteTextViewReflector() {
            try {
                doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Ah well.
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (Exception e) {
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (Exception e) {
                }
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception e) {
                }
            }

            // Hidden method failed, call public version instead
            imm.showSoftInput(view, flags);
        }
    }

    public interface SuggestionsAdapter extends ListAdapter, Filterable {

    }
}

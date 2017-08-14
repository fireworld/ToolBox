package cc.colorcat.toolbox.widget;

import android.annotation.SuppressLint;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cc.colorcat.toolbox.R;


/**
 * Created by cxx on 2016/9/19.
 * xx.ch@outlook.com
 */
public abstract class FragmentSwapperV4 {
    private FragmentManager mManager;
    @AnimRes
    private int[] mForwardAnim = {R.anim.end_in, R.anim.start_out};
    @AnimRes
    private int[] mBackAnim = {R.anim.start_in, R.anim.end_out};
    @IdRes
    private int mViewId;
    private int mCurrent = -1;
    private int mNext = 0;

    private OnChangeListener mListener;

    public FragmentSwapperV4(FragmentManager manager, @IdRes int viewResId) {
        if (manager == null) {
            throw new NullPointerException("fm == null");
        }
        mManager = manager;
        mViewId = viewResId;
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public void setCurrentItem(int position) {
        selectItem(position, false);
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        selectItem(position, true);
    }

    private void selectItem(int position, boolean smoothScroll) {
        checkPosition(position);
        if (position != mCurrent) {
            mNext = position;
            FragmentTransaction ft = mManager.beginTransaction();
            final String currentTag = String.valueOf(mCurrent);
            Fragment currentF = mManager.findFragmentByTag(currentTag);
            if (currentF != null) {
                if (smoothScroll) {
                    setAnimation(ft);
                }
                ft.hide(currentF);
            }
            final String nextTag = String.valueOf(mNext);
            Fragment nextF = mManager.findFragmentByTag(nextTag);
            if (nextF == null) {
                nextF = getItem(mNext);
            }
            if (nextF.isAdded()) {
                ft.show(nextF);
            } else {
                ft.add(mViewId, nextF, nextTag);
            }
            ft.commit();
            mCurrent = mNext;
            notifyFragmentChanged();
        }
    }

    private void notifyFragmentChanged() {
        if (mListener != null) {
            mListener.onChanged(mCurrent);
        }
    }

    private void setAnimation(@NonNull FragmentTransaction ft) {
        if (mNext > mCurrent) {
            ft.setCustomAnimations(mForwardAnim[0], mForwardAnim[1]);
        } else {
            ft.setCustomAnimations(mBackAnim[0], mBackAnim[1]);
        }
    }

    public void setForwardAnimation(@AnimRes int enter, @AnimRes int exit) {
        mForwardAnim[0] = enter;
        mForwardAnim[1] = exit;
    }

    public void setBackAnimation(@AnimRes int enter, @AnimRes int exit) {
        mBackAnim[0] = enter;
        mBackAnim[1] = exit;
    }

    public abstract int getSize();

    @NonNull
    public abstract Fragment getItem(int position);


    @SuppressLint("DefaultLocale")
    private void checkPosition(int position) {
        int size = getSize();
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException(String.format("position[0, %d) = %d", size, position));
        }
    }

    public interface OnChangeListener {

        void onChanged(int position);
    }
}

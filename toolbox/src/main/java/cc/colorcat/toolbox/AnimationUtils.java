package cc.colorcat.toolbox;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by cxx on 17-8-14.
 * xx.ch@outlook.com
 */
public class AnimationUtils {

    public static void rotateForever(@NonNull View view, long durationMillis) {
        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotate);
    }

    private AnimationUtils() {
        throw new AssertionError("no instance");
    }
}

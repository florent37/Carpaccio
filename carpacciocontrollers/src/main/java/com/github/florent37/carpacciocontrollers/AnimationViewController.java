package com.github.florent37.carpacciocontrollers;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class AnimationViewController {

    private static final String TAG = "AnimationViewController";

    private static final String ACCELERATE = "easeIn";
    private static final String DESCELERATE = "easeOut";
    private static final String ACCELERATE_DESCELERATE = "easeInOut";

    protected static BaseInterpolator stringToInterpolator(String s) {
        if (s != null) {
            switch (s) {
                case ACCELERATE:
                    return new AccelerateInterpolator();
                case DESCELERATE:
                    return new DecelerateInterpolator();
                case ACCELERATE_DESCELERATE:
                    return new AccelerateDecelerateInterpolator();
            }
        }
        return null;
    }

    protected static void animate(View view, String parameter, int valueStart, int valueEnd, long duration, String easing) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            android.animation.ObjectAnimator objectAnimator = android.animation.ObjectAnimator.ofFloat(view, parameter, valueStart, valueEnd).setDuration(duration);

            BaseInterpolator interpolator = stringToInterpolator(easing);
            if (interpolator != null)
                objectAnimator.setInterpolator(interpolator);

            objectAnimator.start();
        } else {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, parameter, valueStart, valueEnd).setDuration(duration);

            BaseInterpolator interpolator = stringToInterpolator(easing);
            if (interpolator != null)
                objectAnimator.setInterpolator(interpolator);

            objectAnimator.start();
        }
    }


    public void animateAphaIn(View view, String duration) {
        animateAphaIn(view, duration, null);
    }

    public void animateAphaIn(View view, String duration, String easing) {
        Long dur = CommonViewController.stringToLong(duration);
        if (dur != null) {
            animate(view, "alpha", 0, 1, dur, easing);
        }
    }

    public void animateScaleUp(View view, String duration) {
        animateScaleUp(view, duration, null);
    }

    public void animateScaleUp(View view, String duration, String easing) {
        Long dur = CommonViewController.stringToLong(duration);
        if (dur != null) {
            animate(view, "scaleX", 0, 1, dur, easing);
            animate(view, "scaleY", 0, 1, dur, easing);
        }
    }


    public void animateEnter(View view, String translationX, String translationY, String duration) {
        animateEnterY(view, translationY, duration);
        animateEnterX(view, translationY, duration);
    }

    public void animateEnterY(View view, String translationY, String duration, String easing) {
        Long ty = CommonViewController.stringToLong(translationY);
        Long dur = CommonViewController.stringToLong(duration);
        if (dur != null && ty != null) {
            animate(view, "translationY", ty.intValue(), 0, dur, easing);
        }
    }

    public void animateEnterY(View view, String translationY, String duration) {
        animateEnterY(view, translationY, duration, null);
    }

    public void animateEnterX(View view, String translationX, String duration) {
        animateEnterX(view, translationX, duration, null);
    }

    public void animateEnterX(View view, String translationX, String duration, String easing) {
        Long tx = CommonViewController.stringToLong(translationX);
        Long dur = CommonViewController.stringToLong(duration);
        if (dur != null && tx != null) {
            animate(view, "translationX", tx.intValue(), 0, dur, easing);
        }
    }

}

package com.github.florent37.carpaccio.controllers;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;

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


    public void animateAphaIn(View view, long duration) {
        animateAphaIn(view, duration, null);
    }

    public void animateAphaIn(View view, long duration, String easing) {
        animate(view, "alpha", 0, 1, duration, easing);
    }

    public void animateScaleUp(View view, long duration) {
        animateScaleUp(view, duration, null);
    }

    public void animateScaleUp(View view, long duration, String easing) {
        animate(view, "scaleX", 0, 1, duration, easing);
        animate(view, "scaleY", 0, 1, duration, easing);
    }


    public void animateEnter(View view, int translationX, int translationY, long duration) {
        animateEnterY(view, translationY, duration);
        animateEnterX(view, translationX, duration);
    }

    public void animateEnterY(View view, int translationY, long duration, String easing) {
        animate(view, "translationY", translationY, 0, duration, easing);
    }

    public void animateEnterY(View view, int translationY, long duration) {
        animateEnterY(view, translationY, duration, null);
    }

    public void animateEnterX(View view, int translationX, long duration) {
        animateEnterX(view, translationX, duration, null);
    }

    public void animateEnterX(View view, int translationX, long duration, String easing) {
        animate(view, "translationX", translationX, 0, duration, easing);
    }

}

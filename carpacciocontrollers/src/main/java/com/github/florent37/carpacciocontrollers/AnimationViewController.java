package com.github.florent37.carpacciocontrollers;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class AnimationViewController {

    private static final String TAG = "AnimationViewController";

    private static Long stringToLong(String s){
        try {
            return Long.parseLong(s);
        }catch (NumberFormatException e){
            Log.d(TAG,s+" is not a long",e);
            return null;
        }
    }

    public void animateAphaIn(View view, String duration){
        Long dur = stringToLong(duration);
        if(dur != null){
            ObjectAnimator.ofFloat(view,"alpha",0,1).setDuration(dur).start();
        }
    }

    public void animateScaleUp(View view, String duration){
        Long dur = stringToLong(duration);
        if(dur != null){
            ObjectAnimator.ofFloat(view,"scaleX",0,1).setDuration(dur).start();
            ObjectAnimator.ofFloat(view,"scaleY",0,1).setDuration(dur).start();
        }
    }

    public void animateEnter(View view, String translationX, String translationY, String duration){
        animateEnterY(view,translationY,duration);
        animateEnterX(view, translationY, duration);
    }

    public void animateEnterY(View view, String translationY, String duration){
        Long ty = stringToLong(translationY);
        Long dur = stringToLong(duration);
        if(dur != null && ty != null){
            ObjectAnimator.ofFloat(view,"translationY",ty,0).setDuration(dur).start();
        }
    }

    public void animateEnterX(View view, String translationX, String duration){
        Long tx = stringToLong(translationX);
        Long dur = stringToLong(duration);
        if(dur != null && tx != null){
            ObjectAnimator.ofFloat(view,"translationX",tx,0).setDuration(dur).start();
        }
    }

}

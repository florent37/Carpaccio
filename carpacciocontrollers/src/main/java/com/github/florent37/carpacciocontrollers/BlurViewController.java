package com.github.florent37.carpacciocontrollers;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.blurry.internal.Blur;

/**
 * Created by florentchampigny on 25/07/15.
 */
public class BlurViewController {

    private static final String TAG = "BlurViewController";

    public void blur(View view, String radiusString){
        Integer radius = CommonViewController.stringToInt(radiusString);
        if(radius != null) {
            if (view instanceof ImageView)
                Blurry.with(view.getContext()).radius(radius).async().capture(view).into((ImageView) view);
            else
                Blurry.with(view.getContext()).radius(radius).async().onto(view);
        }
    }

}

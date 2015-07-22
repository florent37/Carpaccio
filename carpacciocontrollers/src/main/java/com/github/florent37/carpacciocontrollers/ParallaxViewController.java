package com.github.florent37.carpacciocontrollers;

import android.view.View;

import com.github.florent37.carpaccio.CarpaccioViewController;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class ParallaxViewController implements ObservableScrollViewCallbacks {

    Map<View,Float> viewsToMove = new HashMap<>();

    public void registerParallax(View view){
        if(view instanceof ObservableScrollView)
            ((ObservableScrollView) view).setScrollViewCallbacks(this);
    }

    public void parallaxY(View view, String y){
        try {
            Float yFloat = Float.valueOf(y);
            viewsToMove.put(view, yFloat);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        for(View view : viewsToMove.keySet()){
            view.setTranslationY(i*viewsToMove.get(view));
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

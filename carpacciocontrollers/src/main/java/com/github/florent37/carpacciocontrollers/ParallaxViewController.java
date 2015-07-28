package com.github.florent37.carpacciocontrollers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florentchampigny on 22/07/15.
 */
public class ParallaxViewController implements ObservableScrollViewCallbacks {

    Map<View, Float> viewsToMove = new HashMap<>();

    public void registerParallax(ScrollView view) {
        registerParallax(view, true);
    }

    public void registerParallax(ScrollView view, boolean replaceWithObservableScrollView) {
        if (replaceWithObservableScrollView && !(view instanceof ObservableScrollView)) {
            CommonViewController replaceViewController = new CommonViewController();
            ObservableScrollView newView = replaceViewController.replaceViewithTagToRemove(view, "com.github.ksoichiro.android.observablescrollview.ObservableScrollView", "registerParallax()");

            if (view.getChildCount() > 0) {
                View scrollViewChild = view.getChildAt(0);
                view.removeView(scrollViewChild);
                newView.addView(scrollViewChild);
            }

            view = newView;
        }

        if (view != null)
            ((ObservableScrollView) view).setScrollViewCallbacks(this);
    }

    public void followScroll(View view) {
        parallaxY(view, 1);
    }

    public void quickReturn(View view) {
        quickReturnTop(view);
    }

    public void quickReturnBottom(View view) {
        //TODO
    }

    public void quickReturnTop(View view) {
        //TODO
    }

    public void parallaxY(View view, float y) {
        viewsToMove.put(view, y);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        for (View view : viewsToMove.keySet()) {
            ViewHelper.setTranslationY(view, i * viewsToMove.get(view));
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

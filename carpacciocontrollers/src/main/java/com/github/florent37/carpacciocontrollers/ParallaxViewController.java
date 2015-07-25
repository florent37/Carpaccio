package com.github.florent37.carpacciocontrollers;

import android.view.View;
import android.widget.ScrollView;

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
        if (!(view instanceof ObservableScrollView)) {
            ReplaceViewController replaceViewController = new ReplaceViewController();
            ObservableScrollView newView = replaceViewController.replace(view, "com.github.ksoichiro.android.observablescrollview.ObservableScrollView");

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

    public void parallaxY(View view, String y) {
        try {
            Float yFloat = Float.valueOf(y);
            viewsToMove.put(view, yFloat);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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

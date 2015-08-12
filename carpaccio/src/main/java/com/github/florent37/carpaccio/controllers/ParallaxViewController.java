package com.github.florent37.carpaccio.controllers;

import android.support.v7.widget.RecyclerView;
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
public class ParallaxViewController {

    Map<View, Float> viewsToMove = new HashMap<>();

    public void registerParallax(View view) {
        if(view instanceof ScrollView)
            registerParallax((ScrollView)view, true);
        else if(view instanceof RecyclerView)
            registerParallaxRecyclerView((RecyclerView) view);
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
            ((ObservableScrollView) view).setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
                @Override
                public void onScrollChanged(int i, boolean b, boolean b1) {
                    scrolled(i);
                }

                @Override
                public void onDownMotionEvent() {

                }

                @Override
                public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                }
            });
    }

    public void registerParallaxRecyclerView(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrolled(recyclerView.computeVerticalScrollOffset());
            }
        });
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

    public void scrolled(int i) {
        for (View view : viewsToMove.keySet()) {
            ViewHelper.setTranslationY(view, i * viewsToMove.get(view));
        }
    }

}

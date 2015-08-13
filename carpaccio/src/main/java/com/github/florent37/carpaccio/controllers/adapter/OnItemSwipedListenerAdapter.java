package com.github.florent37.carpaccio.controllers.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by florentchampigny on 31/07/15.
 */
public abstract class OnItemSwipedListenerAdapter<T> implements OnItemSwipedListener<T> {

    @Override
    public boolean canSwipe(int position, Object object){
        return true;
    }

    @Override
    public boolean onItemSwiped(T item, int position, Holder holder, RecyclerView.Adapter adapter){
        return false;
    }
}

package com.github.florent37.carpaccio.controllers.adapter;

/**
 * Created by florentchampigny on 31/07/15.
 */
public abstract class OnItemClickListenerAdapter<T> implements OnItemClickListener<T>{
    @Override public boolean isClickable(T item, int position, Holder holder){return true;}
    @Override public void onItemClick(T item, int position, Holder holder){}
}

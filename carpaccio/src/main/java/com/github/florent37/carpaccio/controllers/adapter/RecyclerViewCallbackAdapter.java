package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public abstract class RecyclerViewCallbackAdapter<T> implements RecyclerViewCallback<T> {

    public int getItemViewType(int position){return -1;}

    //return a different R.layout.
    public int onCreateViewHolder(int viewType){return -1;}

    public Holder<T> onCreateViewHolder(View view, int viewType){return null;}

    //do a custom action
    public void onBind(Object object, View view, int position){}

}

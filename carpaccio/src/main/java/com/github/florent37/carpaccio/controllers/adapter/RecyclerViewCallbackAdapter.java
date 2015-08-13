package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public abstract class RecyclerViewCallbackAdapter implements RecyclerViewCallback {

    @Override
    public int getItemViewType(int position){return -1;}

    //return a different R.layout.
    @Override
    public int onCreateViewHolder(int viewType){return -1;}

    @Override
    public Holder onCreateViewHolder(View view, int viewType){return null;}

    //do a custom action
    @Override
    public void onBind(Object object, Holder holder, int position){}

}

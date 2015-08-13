package com.github.florent37.carpaccio.controllers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public class Holder extends RecyclerView.ViewHolder {

    int position;
    Object mappedObject;

    public Holder(View itemView) {
        super(itemView);
    }

    public void onBind(Object object){

    }

}
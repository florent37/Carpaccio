package com.github.florent37.carpaccio.controllers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public class Holder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    int position;
    Object mappedObject;
    OnItemClickListener onItemClickListener;

    public Holder(View itemView) {
        super(itemView);
        this.itemView.setOnClickListener(this);
    }

    public void onBind(T object){

    }

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null)
            onItemClickListener.onItemClick(mappedObject, position, itemView);
    }

}
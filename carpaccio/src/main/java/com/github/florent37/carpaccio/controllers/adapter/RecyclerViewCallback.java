package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by florentchampigny on 31/07/15.
 */
public interface RecyclerViewCallback<T> {

    int getItemViewType(int position);

    //return a different R.layout.
    int onCreateViewHolder(int viewType);

    Holder<T> onCreateViewHolder(View view, int viewType);

    //do a custom action
    void onBind(Object object, View view, int position);

}

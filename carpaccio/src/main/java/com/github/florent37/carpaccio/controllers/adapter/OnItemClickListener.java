package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public interface OnItemClickListener<T> {
    boolean isClickable(T item, int position, Holder holder);
    void onItemClick(T item, int position, Holder holder);
}

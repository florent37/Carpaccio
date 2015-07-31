package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;

/**
 * Created by florentchampigny on 31/07/15.
 */
public interface OnItemClickListener<T> {
    void onItemClick(T item, int position, View view);
}

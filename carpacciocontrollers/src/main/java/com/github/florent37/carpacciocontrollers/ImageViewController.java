package com.github.florent37.carpacciocontrollers;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageViewController {

    public void url(ImageView view, String url) {
        ImageView imageView = (ImageView) view;
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

}

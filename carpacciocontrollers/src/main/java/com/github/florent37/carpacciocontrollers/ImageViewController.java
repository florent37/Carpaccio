package com.github.florent37.carpacciocontrollers;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageViewController {

    public void url(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

}

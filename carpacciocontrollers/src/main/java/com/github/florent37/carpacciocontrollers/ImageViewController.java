package com.github.florent37.carpacciocontrollers;

import android.view.View;
import android.widget.ImageView;

import com.github.florent37.carpaccio.CarpaccioViewController;
import com.squareup.picasso.Picasso;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageViewController extends CarpaccioViewController {

    public void url(View view, String url) {
        if(view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
    }

}

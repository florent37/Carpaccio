package com.github.florent37.badview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ImageBadView {

    public void url(View view, String url) {
        if(view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
    }

}

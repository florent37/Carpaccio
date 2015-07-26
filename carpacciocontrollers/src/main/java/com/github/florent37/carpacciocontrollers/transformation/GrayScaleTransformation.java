package com.github.florent37.carpacciocontrollers.transformation;

import android.graphics.Bitmap;

import com.github.florent37.carpacciocontrollers.helper.GrayScaleHelper;
import com.squareup.picasso.Transformation;

/**
 * Created by florentchampigny on 26/07/15.
 */
public class GrayScaleTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        return GrayScaleHelper.greyScale(source);
    }

    @Override
    public String key() {
        return "GrayscaleTransformation";
    }
}
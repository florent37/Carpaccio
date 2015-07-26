package com.github.florent37.carpacciocontrollers.transformation;

import android.graphics.Bitmap;

import com.github.florent37.carpacciocontrollers.helper.FastBlurHelper;
import com.squareup.picasso.Transformation;

/**
 * Created by florentchampigny on 26/07/15.
 */
public class BlurTransformation implements Transformation {

    int radius;

    public BlurTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = FastBlurHelper.doBlur(source, 10, false);

        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "BlurTransformation";
    }
}
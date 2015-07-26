package com.github.florent37.carpacciocontrollers;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.github.florent37.carpacciocontrollers.helper.FastBlurHelper;

/**
 * Created by florentchampigny on 25/07/15.
 */
public class BlurViewController {

    private static final String TAG = "BlurViewController";

    public void blur(final ImageView imageView, final String radiusString) {

        imageView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap newBitmap = FastBlurHelper.getBitmapBlurFromView(imageView, CommonViewController.stringToInt(radiusString));
                    imageView.setImageBitmap(newBitmap);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

    }

}

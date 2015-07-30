package com.github.florent37.carpaccio.controllers.helper;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by florentchampigny on 26/07/15.
 */
public class ViewHelper {
    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());

        view.setDrawingCacheEnabled(false);
        return returnedBitmap;
    }
}

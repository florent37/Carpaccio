package com.github.florent37.carpaccio.controllers.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by florentchampigny on 26/07/15.
 */
public class GrayScaleHelper {

    public static Bitmap getBitmapGreyScaleFromView(View view) {
        Bitmap bitmap = ViewHelper.getBitmapFromView(view);
        Bitmap newBitmap = GrayScaleHelper.greyScale(bitmap);
        if(bitmap != newBitmap){
            bitmap.recycle();
        }
        return newBitmap;
    }

    public static Bitmap greyScale(Bitmap source){
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        ColorMatrix saturation = new ColorMatrix();
        saturation.setSaturation(0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(saturation));
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();

        if(source != bitmap){
            source.recycle();
        }

        return bitmap;
    }
}

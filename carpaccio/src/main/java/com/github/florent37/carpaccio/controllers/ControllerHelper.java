package com.github.florent37.carpaccio.controllers;

import android.content.Context;
import android.util.Log;

import com.github.florent37.carpaccio.Carpaccio;

import java.lang.reflect.Field;

/**
 * Created by florentchampigny on 31/07/15.
 */
public class ControllerHelper {

    private static final String TAG = "ControllerHelper";

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int getLayoutIdentifierFromString(Context context, String layoutName) {
        if(layoutName.startsWith("R.layout."))
            layoutName = layoutName.replace("R.layout.","");

        if(Carpaccio.IN_EDIT_MODE){
            try {
                Class test = Class.forName(context.getPackageName()+".R$layout");
                Field field = test.getDeclaredField(layoutName);
                int value = field.getInt(null);
                return value;
            } catch (Exception e) {
                Log.e(TAG,e.getMessage(),e);
            }

        }

        final int layoutResId = context.getResources().getIdentifier(layoutName, "layout", context.getPackageName());
        if(layoutResId == 0) {
            Log.e(TAG, "failed to find view layout " + layoutName);
            return -1;
        }
        return layoutResId;
    }

}

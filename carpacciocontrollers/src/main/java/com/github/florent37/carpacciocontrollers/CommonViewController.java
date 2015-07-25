package com.github.florent37.carpacciocontrollers;

import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class CommonViewController {

    private static final String TAG = "CommonViewController";

    public static Integer stringToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Log.d(TAG, s + " is not an integer", e);
            return null;
        }
    }

    public static Long stringToLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            Log.d(TAG, s + " is not a long", e);
            return null;
        }
    }

    public static Float stringToFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            Log.d(TAG, s + " is not a long", e);
            return null;
        }
    }

    public void padding(View view, String paddingLeft, String paddingTop, String paddingRight, String paddingBottom){

    }
}

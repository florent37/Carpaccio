package com.github.florent37.carpacciocontrollers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class CommonViewController {

    private static final String TAG = "CommonViewController";

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void padding(View view, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        view.setPadding(
                (int)pxFromDp(view.getContext(),paddingLeft),
                (int)pxFromDp(view.getContext(),paddingTop),
                (int)pxFromDp(view.getContext(),paddingRight),
                (int)pxFromDp(view.getContext(),paddingBottom)
        );
    }

    public void margin(View view, int marginLeft, int marginTop, int marginRight, int marginBottom){
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.setMargins(
                    (int)pxFromDp(view.getContext(), marginLeft),
                    (int)pxFromDp(view.getContext(), marginTop),
                    (int)pxFromDp(view.getContext(), marginRight),
                    (int)pxFromDp(view.getContext(), marginBottom)
            );
            view.setLayoutParams(lp);
        }
    }
}

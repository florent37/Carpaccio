package com.github.florent37.carpacciocontrollers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpacciocontrollers.adapter.CarpaccioRecyclerViewAdapter;

import java.util.List;

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

    public void adapter(View view, String mappedName, String layoutName){
        Context context = view.getContext();

        if(layoutName.startsWith("R.layout."))
            layoutName = layoutName.replace("R.layout.","");

        final int layoutResId = context.getResources().getIdentifier(layoutName, "layout", context.getPackageName());
        if(layoutResId == 0) {
            Log.e(TAG, "failed to find view layout " + layoutName);
            return;
        }

        final Carpaccio carpaccio = CarpaccioHelper.findParentCarpaccio(view);

        if(carpaccio != null) {
            if (view instanceof RecyclerView) {
                ((RecyclerView) view).setLayoutManager(new LinearLayoutManager(view.getContext()));
                ((RecyclerView) view).setAdapter(new CarpaccioRecyclerViewAdapter(carpaccio, layoutResId, mappedName));
            } else if (view instanceof AdapterView) {

            }
        }
    }
}

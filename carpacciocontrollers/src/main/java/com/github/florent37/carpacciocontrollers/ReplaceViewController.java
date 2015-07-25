package com.github.florent37.carpacciocontrollers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class ReplaceViewController {

    public static final String TAG = "ReplaceViewController";

    public <T extends View> T replace(View view, String viewIdentifier) {
        Context context = view.getContext();
        View newView = null;
        if(viewIdentifier.startsWith("R.layout.") || !viewIdentifier.contains("."))
            newView = replaceByLayout(context, view,viewIdentifier);
        else
            newView = replaceByViewClass(context, view,viewIdentifier);

        if(newView != null) {
            ViewGroup parent = (ViewGroup) view.getParent();

            newView.setLayoutParams(view.getLayoutParams());
            newView.setId(view.getId());
            if(view.getBackground() != null)
                newView.setBackgroundDrawable(view.getBackground());

            parent.removeView(view);
            parent.addView(newView);
        }

        return (T)newView;

        //TODO inject params (text, etc)
    }

    protected View replaceByViewClass(Context context, View view, String viewClassName) { //com.github.florent37.materialviewpager.MaterialViewPager
        View newView = null;
        try{
            Class viewClass = Class.forName(viewClassName);
            Constructor<View> constructor = viewClass.getDeclaredConstructor(Context.class);
            newView = constructor.newInstance(context);
        }catch (Exception e){
            Log.e(TAG,"failed to find view class "+viewClassName,e);
        }

        return newView;
    }

    protected View replaceByLayout(Context context, View view, String layout) { //R.layout.XXX
        if(layout.startsWith("R.layout."))
            layout = layout.replace("R.layout.","");

        int layoutResId = context.getResources().getIdentifier(layout, "layout", context.getPackageName());
        if(layoutResId == 0) {
            Log.e(TAG, "failed to find view layout " + layout);
            return null;
        }
        else {
            ViewGroup parent = (ViewGroup) view.getParent();
            return LayoutInflater.from(view.getContext()).inflate(layoutResId, parent, false);
        }
    }

}

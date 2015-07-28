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

import com.github.florent37.carpaccio.CarpaccioHelper;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import java.lang.reflect.Constructor;
import java.util.List;

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

            newView.setTag(CarpaccioHelper.removeTag(view,"replace(" + viewIdentifier + ")"));

            if(view.getBackground() != null)
                newView.setBackgroundDrawable(view.getBackground());

            if(view instanceof ImageView && newView instanceof ImageView){
                ImageView imgView = (ImageView) view;
                ImageView imgNewView = (ImageView) newView;

                if(imgView.getDrawable() != null)
                    imgNewView.setImageDrawable(imgView.getDrawable());
            }

            if(view instanceof TextView && newView instanceof TextView){
                TextView txtView = (TextView) view;
                TextView txtNewView = (TextView) newView;

                if(txtView.getText() != null)
                    txtNewView.setText(txtView.getText());
                if(txtView.getHint() != null)
                    txtNewView.setHint(txtView.getText());
            }

            parent.removeView(view);
            parent.addView(newView);
        }

        CarpaccioHelper.registerToParentCarpaccio(newView);

        return (T)newView;
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

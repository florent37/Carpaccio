package com.github.florent37.carpacciocontrollers;

import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class TextViewController {

    private static LruCache<String, Typeface> TYPEFACE_CACHE = new LruCache<String, Typeface>(12);

    //public void font(View view, String typeface){
    //    if(view instanceof TextView) {
    //        TextView textView = (TextView) view;
    //        setTypeFace(textView,typeface);
    //    }
    //}

    public void font(TextView textView, String typeface) {
        setTypeFace(textView, typeface);
    }

    public void setText(TextView textView, String value) {
        textView.setText(value);
    }

    protected void setTypeFace(TextView textView, String fontName) {
        if (fontName != null) {
            try {
                //on regarde dans le cache si cette police est présente
                Typeface typeface = TYPEFACE_CACHE.get(fontName);

                //si non, on la charge à partir des assets
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + fontName);

                    //puis on la sauvegarde dans notre cache
                    TYPEFACE_CACHE.put(fontName, typeface);
                }

                //puis on l'utilise sur notre TextView
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }

}

package com.github.florent37.carpaccio.controllers;

import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.TextView;

import com.github.florent37.carpaccio.controllers.helper.LoremHelper;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class TextViewController {

    private static LruCache<String, Typeface> TYPEFACE_CACHE = new LruCache<String, Typeface>(12);

    public void setText(TextView textView, String value) {
        textView.setText(value);
    }

    public void text(TextView textView, String value) {
        setText(textView, value);
    }

    public void setFont(TextView textView, String fontName) {
        font(textView, fontName);
    }

    public void font(TextView textView, String fontName) {
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

    public void lorem(TextView textView) {
        textView.setText(LoremHelper.LOREM);
    }

}

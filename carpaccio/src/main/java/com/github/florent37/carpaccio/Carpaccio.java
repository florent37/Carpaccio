package com.github.florent37.carpaccio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.carpaccio.mapping.MappingManager;

import java.util.List;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class Carpaccio extends FrameLayout {

    protected CarpaccioManager carpaccioManager;
    public static boolean IN_EDIT_MODE = false;

    protected void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BadView);

        {
            String register = styledAttrs.getString(R.styleable.BadView_register);
            if (register != null && carpaccioManager != null) {
                carpaccioManager.registerControllers(register);
            }
        }

        styledAttrs.recycle();
    }

    public Carpaccio(Context context) {
        super(context);
        carpaccioManager = new CarpaccioManager(new MappingManager());
    }

    public Carpaccio(Context context, AttributeSet attrs) {
        super(context, attrs);
        carpaccioManager = new CarpaccioManager(new MappingManager());
        handleAttributes(context, attrs);
    }

    public Carpaccio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        carpaccioManager = new CarpaccioManager(new MappingManager());
        handleAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        IN_EDIT_MODE = isInEditMode();

        if (carpaccioManager != null) {
            carpaccioManager.findCarpaccioControlledViews(this);
            carpaccioManager.executeActionsOnViews();
        }
    }

    public CarpaccioManager getCarpaccioManager() {
        return carpaccioManager;
    }

    public void mapObject(String name, Object object) {
        if (carpaccioManager != null)
            carpaccioManager.mapObject(name, object);
    }

    //region mapList

    public void registerAdapter(String mappedName, Object adapter) {
        if (carpaccioManager != null)
            carpaccioManager.registerAdapter(mappedName, adapter);
    }

    public void mapList(String name, List list) {
        if (carpaccioManager != null)
            carpaccioManager.mapList(name, list);
    }

    public List getMappedList(String mapName) {
        if (carpaccioManager != null)
            return carpaccioManager.getMappedList(mapName);
        return null;
    }

    public void addChildViews(View view) {
        if (carpaccioManager != null)
            carpaccioManager.addChildViews(view);
    }

    public void bindView(View view, String mapName, int position) {
        if (carpaccioManager != null)
            carpaccioManager.bindView(view, mapName, position);
    }

    public void addCarpaccioView(View view) {
        if(carpaccioManager != null) {
            //carpaccioManager.addView(view); TODO
            carpaccioManager.executeActionsOnView(view);
        }
    }

    //endregion
}

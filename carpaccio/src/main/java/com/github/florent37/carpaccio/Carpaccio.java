package com.github.florent37.carpaccio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.carpaccio.mapping.MappingManager;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class Carpaccio extends FrameLayout {

    protected CarpaccioManager carpaccioManager;

    protected void findCarpaccioControlledViews(View view) {
        if (carpaccioManager.isCarpaccioControlledView(view))
            carpaccioManager.addView(view);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ViewGroup.class.cast(view);
            for (int i = 0; i < viewGroup.getChildCount(); ++i)
                findCarpaccioControlledViews(viewGroup.getChildAt(i));
        }
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
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
        findCarpaccioControlledViews(this);

        if (carpaccioManager != null)
            carpaccioManager.executeActionsOnViews();
    }

    public CarpaccioManager getCarpaccioManager() {
        return carpaccioManager;
    }

    public void mapObject(String name, Object object) {
        if (carpaccioManager != null)
            carpaccioManager.mapObject(name, object);
    }

}

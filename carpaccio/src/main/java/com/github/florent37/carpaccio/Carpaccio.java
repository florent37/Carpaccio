package com.github.florent37.carpaccio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.carpaccio.mapping.MappingManager;
import com.github.florent37.carpaccio.mapping.MappingWaiting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class Carpaccio extends FrameLayout {

    protected List<View> carpaccioViews = new ArrayList<>();
    protected List<Object> registerControllers = new ArrayList<>();

    protected MappingManager mappingManager;

    protected void findBadViews(View view) {
        if (view.getTag() != null && !view.getTag().toString().isEmpty())
            carpaccioViews.add(view);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ViewGroup.class.cast(view);
            for (int i = 0; i < viewGroup.getChildCount(); ++i)
                findBadViews(viewGroup.getChildAt(i));
        }
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.BadView);

        {
            String register = styledAttrs.getString(R.styleable.BadView_register);
            if (register != null) {
                String[] registers = register.split(";");
                for (String s : registers) {
                    String reg = s.trim().replace(";", "");
                    Object registerController = CarpaccioHelper.construct(reg);
                    if (registerController != null)
                        registerControllers.add(registerController);
                }
            }

        }

        styledAttrs.recycle();

    }

    public Carpaccio(Context context) {
        super(context);
    }

    public Carpaccio(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
    }

    public Carpaccio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findBadViews(this);

        mappingManager = new MappingManager(this);

        for (View view : carpaccioViews) {
            String tag = view.getTag().toString().trim();
            String[] calls = CarpaccioHelper.trim(tag.split(";"));
            for (String call : calls) {
                String function = CarpaccioHelper.getFunctionName(call);
                String[] args = CarpaccioHelper.getAttributes(call);
                if (mappingManager.isCallMapping(args))
                    mappingManager.callMapping(function, view, args);
                else
                    callFunctionOnControllers(function, view, args);
            }
        }
    }

    public void callFunctionOnControllers(final String function, final View view, final String[] args) {
        CarpaccioHelper.callFunctionOnObjects(this.registerControllers,function,view,args);
    }

    public void mapObject(String name, Object object) {
        mappingManager.mapObject(name,object);
    }

}

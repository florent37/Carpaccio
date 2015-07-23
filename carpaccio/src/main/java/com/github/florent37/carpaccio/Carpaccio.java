package com.github.florent37.carpaccio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class Carpaccio extends FrameLayout {

    List<View> badViews = new ArrayList<>();
    List<Object> registerObjects = new ArrayList<>();

    protected void findBadViews(View view) {
        if (view.getTag() != null && !view.getTag().toString().isEmpty())
            badViews.add(view);

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
                    Object registerObject = CarpaccioHelper.construct(reg);
                    if (registerObject != null)
                        registerObjects.add(registerObject);
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

        for (View view : badViews) {
            String tag = view.getTag().toString().trim();
            String[] calls = CarpaccioHelper.trim(tag.split(";"));
            for (String call : calls) {
                String function = CarpaccioHelper.getFunctionName(call);
                String[] args = CarpaccioHelper.getAttributes(call);
                if (isCallMapping(args))
                    callMapping(function, view, args);
                else
                    callFunction(function, view, args);
            }
        }
    }

    protected void callFunction(final String function, final View view, final String[] args) {
        for (Object registerObject : registerObjects) {
            if (registerObject != null)
                CarpaccioHelper.callFunction(registerObject, function, view, args);
        }
    }

    public class MappingWaiting {
        public View view;
        public String function;
        public String arg;
        public String objectName;

        public MappingWaiting(View view, String function, String arg, String objectName) {
            this.view = view;
            this.function = function;
            this.arg = arg;
            this.objectName = objectName;
        }
    }

    protected Map<String, Object> mappedObjects = new HashMap<>();
    protected Map<String, List<MappingWaiting>> mappingWaitings = new HashMap<>();

    public void mapObject(String name, Object object) {
        mappedObjects.put(name, object);

        //call the waiting objects
        List<MappingWaiting> waitings = mappingWaitings.get(name);
        if (waitings != null) {
            for (MappingWaiting mappingWaiting : waitings) {
                if(mappingWaiting.objectName.equals(name)) {
                    String value = object.toString();
                    callFunction(mappingWaiting.function, mappingWaiting.view, new String[]{value});

                    mappingWaitings.remove(name);
                }
            }
        }
    }

    protected boolean isCallMapping(String[] args) {
        return args.length == 1 && args[0].startsWith("$");
    }

    protected void callMapping(String function, View view, String[] args) {
        if (isCallMapping(args)) {
            String arg = args[0];
            String objectName = arg.substring(1, arg.length()); //"user"

            { //add to waiting
                List<MappingWaiting> waitings = mappingWaitings.get(objectName);
                if (waitings == null)
                    waitings = new ArrayList<>();
                waitings.add(new MappingWaiting(view, function, arg, objectName));
                mappingWaitings.put(objectName, waitings);
            }
        }
    }
}

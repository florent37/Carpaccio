package com.github.florent37.carpaccio;

import android.view.View;

import com.github.florent37.carpaccio.mapping.MappingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioManager implements MappingManager.MappingManagerCallback {

    protected List<View> carpaccioViews = new ArrayList<>();
    protected List<Object> registerControllers = new ArrayList<>();
    protected MappingManager mappingManager;

    public CarpaccioManager(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
        this.mappingManager.setMappingManagerCallback(this);
    }

    public void addView(View view) {
        carpaccioViews.add(view);
    }

    public boolean isCarpaccioControlledView(View view) {
        if(view.getTag() != null){
            String tag = view.getTag().toString();
            return !tag.isEmpty() && tag.contains("(") && tag.contains(")"); //TODO use matcher
        }else
            return false;
    }

    public void registerController(Object controller) {
        if (controller != null)
            registerControllers.add(controller);
    }

    public void registerControllers(String registerString) {
        String[] registers = registerString.split(";");
        for (String s : registers) {
            String reg = s.trim().replace(";", "");
            if (!reg.isEmpty())
                registerController(CarpaccioHelper.construct(reg));
        }
    }

    public void executeActionsOnViews() {
        for (View view : carpaccioViews) {
            String tag = view.getTag().toString().trim();
            String[] calls = CarpaccioHelper.trim(tag.split(";"));
            for (String call : calls) {
                if(!call.startsWith("//")) {
                    String function = CarpaccioHelper.getFunctionName(call);
                    String[] args = CarpaccioHelper.getAttributes(call);

                    //if it's a mapped function ex: setText($user)
                    if (mappingManager != null && mappingManager.isCallMapping(args))
                        mappingManager.callMapping(function, view, args);
                    else
                        //an usual function setText(florent)
                        callFunctionOnControllers(function, view, args);
                }
            }
        }
    }

    public void callFunctionOnControllers(final String function, final View view, final String[] args) {
        CarpaccioHelper.callFunctionOnObjects(this.registerControllers, function, view, args);
    }

    public void mapObject(String name, Object object) {
        if (mappingManager != null)
            mappingManager.mapObject(name, object);
    }

}
